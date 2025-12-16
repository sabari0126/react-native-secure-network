package com.securenetwork.utils


import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

interface ResultData {
  val isSecureNetwork: Boolean
  val message: String
}

enum class NetworkResult(override val isSecureNetwork: Boolean, override val message: String) : ResultData {
  LOCATION_PERMISSION_DENIED(false, "Location permission denied"),
  LOCATION_SERVICES_DISABLED(false, "Location services are disabled"),

  WIFI_UNSECURED_OPEN(false, "Connected to an unsecured open Wi-Fi network"),
  WIFI_UNSECURED_WEP(false, "Connected to a weak security Wi-Fi network (WEP)"),
  WIFI_UNSECURED_WPS(false, "Connected to a Wi-Fi network using insecure WPS"),
  WIFI_UNSECURED_WPA(false, "Connected to a weak security Wi-Fi network (WPA-1)"),

  WIFI_NOT_CONNECTED(true, "Using mobile data or not connected to Wi-Fi"),
  WIFI_INFO_UNAVAILABLE(true, "Wi-Fi information is unavailable"),
  WIFI_SECURE_NETWORK(true, "Connected to a secure Wi-Fi network")
}

fun NetworkResult.toResponse() = NetworkResponse(this.isSecureNetwork, this.message)

data class NetworkResponse(val isSecureNetwork: Boolean, val message: String)

fun NetworkResponse.toWritableMap(): WritableMap {
  val map = Arguments.createMap()
  map.putBoolean("isSecureNetwork", this.isSecureNetwork)
  map.putString("message", this.message)
  return map
}
