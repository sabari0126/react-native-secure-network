package com.securenetwork.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.securenetwork.utils.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

@SuppressLint("MissingPermission")
class WifiSecurityChecker(private val context: Context) {

  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  private val wifiManager =
    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

  companion object {
    const val WEP = "WEP"
    const val WPS = "WPS"
    const val WPA = "WPA"
    const val WPA2 = "WPA2"
    const val WPA3 = "WPA3"
  }

  fun getNetworkStatus(callback: (NetworkResult) -> Unit) {
    if (!hasLocationPermission()) {
      callback(NetworkResult.LOCATION_PERMISSION_DENIED)
      return
    }
    if (!isLocationEnabled()) {
      callback(NetworkResult.LOCATION_SERVICES_DISABLED)
      return
    }
    if (!isWifiConnected()) {
      callback(NetworkResult.WIFI_NOT_CONNECTED)
      return
    }

    CoroutineScope(Dispatchers.IO).launch {
      val currentBssid = getCurrentBssid()
      if (currentBssid.isEmpty()) {
        withContext(Dispatchers.Main) {
          callback(NetworkResult.WIFI_INFO_UNAVAILABLE)
        }
        return@launch
      }

      val scanResults = wifiManager.scanResults
      val currentNetwork =
        scanResults.find { it.BSSID.equals(currentBssid, ignoreCase = true) }

      val result = if (currentNetwork != null) {
        val caps = currentNetwork.capabilities.uppercase()

        when {
          checkOpenNetwork(caps) -> NetworkResult.WIFI_UNSECURED_OPEN
          has(caps, WEP) -> NetworkResult.WIFI_UNSECURED_WEP
          has(caps, WPS) -> NetworkResult.WIFI_UNSECURED_WPS
          has(caps, WPA2) || has(caps, WPA3) -> NetworkResult.WIFI_SECURE_NETWORK
          has(caps, WPA) -> NetworkResult.WIFI_UNSECURED_WPA
          else -> NetworkResult.WIFI_INFO_UNAVAILABLE
        }
      } else {
        NetworkResult.WIFI_INFO_UNAVAILABLE
      }

      withContext(Dispatchers.Main) {
        callback(result)
      }
    }
  }


  private suspend fun getCurrentBssid(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val wifiResult = getWifiInfo()
      wifiResult?.second?.bssid ?: ""
    } else {
      val wifiInfo = wifiManager.connectionInfo
      wifiInfo?.bssid ?: ""
    }
  }

  @RequiresApi(Build.VERSION_CODES.S)
  private suspend fun getWifiInfo(): Pair<String, WifiInfo>? =
    suspendCancellableCoroutine { continuation ->

      val request =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
          .build()

      val networkCallback = object : ConnectivityManager.NetworkCallback(
        FLAG_INCLUDE_LOCATION_INFO
      ) {
        override fun onCapabilitiesChanged(
          network: Network,
          networkCapabilities: NetworkCapabilities,
        ) {
          super.onCapabilitiesChanged(network, networkCapabilities)

          val linkAddresses =
            connectivityManager.getLinkProperties(network)?.linkAddresses
          linkAddresses?.firstOrNull { linkAddress ->
            linkAddress.address.hostAddress?.contains('.') ?: false
          }?.address?.hostAddress?.let { ipV4Address ->
            val wifiInfo = networkCapabilities.transportInfo as? WifiInfo
            if (wifiInfo != null && continuation.isActive) {
              continuation.resume(Pair(ipV4Address, wifiInfo)) {}
              connectivityManager.unregisterNetworkCallback(this)
            }
          }
        }
      }

      connectivityManager.registerNetworkCallback(request, networkCallback)

      continuation.invokeOnCancellation {
        connectivityManager.unregisterNetworkCallback(networkCallback)
      }
    }

  private fun checkOpenNetwork(caps: String): Boolean {
    return !containsSecureProtocol(caps) && !has(caps, WEP) && !has(caps, WPS)
  }

  private fun containsSecureProtocol(caps: String): Boolean {
    return has(caps, WPA) || has(caps, WPA2) || has(caps, WPA3)
  }

  private fun has(caps: String, keyword: String) = caps.contains(keyword)


  private fun isWifiConnected(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val network = connectivityManager.activeNetwork ?: return false
      val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
      caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
      val networkInfo = connectivityManager.activeNetworkInfo
      networkInfo?.type == ConnectivityManager.TYPE_WIFI
    }
  }

  private fun hasLocationPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(
      context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
  }

  private fun isLocationEnabled(): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      locationManager.isLocationEnabled
    } else {
      locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
      )
    }
  }
}
