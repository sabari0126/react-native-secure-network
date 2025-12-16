package com.securenetwork


import com.facebook.react.bridge.ReactApplicationContext

abstract class SecureNetworkSpec internal constructor(context: ReactApplicationContext) :
  NativeSecureNetworkSpec(context) {
}
