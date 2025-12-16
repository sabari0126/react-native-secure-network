package com.securenetwork

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.module.annotations.ReactModule
import com.securenetwork.utils.toResponse
import com.securenetwork.utils.toWritableMap
import com.securenetwork.wifi.WifiSecurityChecker

class SecureNetworkModule(reactContext: ReactApplicationContext) :
  SecureNetworkSpec(reactContext) {

  private val wifiChecker = WifiSecurityChecker(reactContext)

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun getConnectionStatus(promise: Promise) {
    try {
      wifiChecker.getNetworkStatus { networkResult ->
        val map: WritableMap = networkResult.toResponse().toWritableMap()
        promise.resolve(map)
      }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  companion object {
    const val NAME = "SecureNetwork"
  }
}
