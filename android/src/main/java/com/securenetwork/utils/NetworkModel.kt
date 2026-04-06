package com.securenetwork.utils


import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

interface ResultData {
  val isSecureNetwork: Boolean
  val message: String
  val code: Int
}

enum class NetworkResult(
  override val isSecureNetwork: Boolean,
  override val message: String,
  override val code: Int
) : ResultData {

  WIFI_UNSECURED_OPEN(false, "Connected to an unsecured open Wi-Fi network", 9001),
  WIFI_UNSECURED_WEP(false, "Connected to a weak security Wi-Fi network (WEP)", 9002),
  WIFI_UNSECURED_WPS(false, "Connected to a Wi-Fi network using insecure WPS", 9003),
  WIFI_UNSECURED_WPA(false, "Connected to a weak security Wi-Fi network (WPA-1)", 9004),
  LOCATION_PERMISSION_DENIED(false, "Location permission denied", 9005),
  LOCATION_SERVICES_DISABLED(false, "Location services are disabled", 9006),

  WIFI_SECURE_NETWORK(true, "Connected to a secure Wi-Fi network", 7001),
  WIFI_NOT_CONNECTED(true, "Using mobile data or not connected to Wi-Fi", 7002),
  WIFI_INFO_UNAVAILABLE(true, "Wi-Fi information is unavailable", 7003)
}

fun NetworkResult.toResponse() = NetworkResponse(this.isSecureNetwork, this.message, this.code)

data class NetworkResponse(val isSecureNetwork: Boolean, val message: String, val code: Int)

fun NetworkResponse.toWritableMap(): WritableMap {
  val map = Arguments.createMap()
  map.putBoolean("isSecureNetwork", this.isSecureNetwork)
  map.putString("message", this.message)
  map.putInt("code", this.code)
  return map
}
