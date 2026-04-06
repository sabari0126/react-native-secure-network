# react-native-secure-network

**react-native-secure-network** is a simple and reliable React Native library designed to assess the security of the device's active network connection. It identifies whether the network is Wi-Fi or mobile data, checks if the Wi-Fi network is open or protected, and inspects the encryption type (such as WPA/WPA2). This allows developers to block or warn users when their device is connected to an insecure network.

## 📦 Installation

**NPM:**
```bash
npm install react-native-secure-network
```

**Yarn:**
```bash
yarn add react-native-secure-network
```

## 🚀 Usage

Import the react-native-secure-network library:
```typescript
import Network, { ResponseCode } from 'react-native-secure-network';
```

> **Note:** This library provides only promise-based functions.

### Using Promises
```typescript
Network.getConnectionStatus()
  .then((response) => {
    // Handle success
    console.log(response);
  })
  .catch((error) => {
    // Handle error
    console.error(error);
  });
```

### Using Async/Await
```typescript
async function checkNetwork() {
  try {
    const response = await Network.getConnectionStatus();
    
    switch (response.code) {
      case ResponseCode.WIFI_SECURE:
        // Safe to proceed
        break;
      case ResponseCode.WIFI_UNSECURED_OPEN:
      case ResponseCode.WIFI_UNSECURED_WEP:
      case ResponseCode.WIFI_UNSECURED_WPA:
        // Warn the user about insecure network
        console.warn(response.message);
        break;
      case ResponseCode.PERMISSION_DENIED:
      case ResponseCode.PERMISSION_NOT_DETERMINED:
        // Prompt the user for location permission
        break;
      default:
        console.log(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
```

## 📚 API Reference

### Available Methods

| Method                  | Return Type | iOS | Android |
| ----------------------- | ----------- | :-: | :-----: |
| `getConnectionStatus()` | `Promise`   | ✅  |   ✅    |

### `getConnectionStatus()`

Checks the security status of the device's current network connection.

**Usage:**
```typescript
const response = await Network.getConnectionStatus();
```

**Response Object:**
```typescript
{
  isSecureNetwork: boolean;
  message: string;
  code: ResponseCode;
}
```

**Response Codes:**

Each response includes a unique numeric `code` to differentiate between outcomes:

- **7xxx** — Secure / safe responses (`isSecureNetwork: true`)
- **9xxx** — Insecure / unknown responses (`isSecureNetwork: false`)

| Code | Enum                        | `isSecureNetwork` | Platform | Message                                            |
| ---- | --------------------------- | :---------------: | :------: | -------------------------------------------------- |
| 7001 | `WIFI_SECURE`               |      `true`       |   Both   | Connected to a secure Wi-Fi network                |
| 7002 | `WIFI_NOT_CONNECTED`        |      `true`       | Android  | Using mobile data or not connected to Wi-Fi        |
| 7003 | `WIFI_INFO_UNAVAILABLE`     |      `true`       |   Both   | Wi-Fi information is unavailable                   |
| 7004 | `UNKNOWN`                   |      `true`       |   iOS    | Unable to determine network security status        |
| 9001 | `WIFI_UNSECURED_OPEN`       |      `false`      |   Both   | Connected to an unsecured open Wi-Fi network       |
| 9002 | `WIFI_UNSECURED_WEP`        |      `false`      | Android  | Connected to a weak security Wi-Fi network (WEP)   |
| 9003 | `WIFI_UNSECURED_WPS`        |      `false`      | Android  | Connected to a Wi-Fi network using insecure WPS    |
| 9004 | `WIFI_UNSECURED_WPA`        |      `false`      | Android  | Connected to a weak security Wi-Fi network (WPA-1) |
| 9005 | `PERMISSION_DENIED`         |      `false`      | Android  | Location permission denied                         |
| 9006 | `LOCATION_SERVICES_DISABLED`|      `false`      | Android  | Location services are disabled                     |
| 9007 | `PERMISSION_NOT_DETERMINED` |      `false`      |   iOS    | Location permission required for network analysis  |
| 9008 | `UNSUPPORTED_OS_VERSION`    |      `false`      |   iOS    | iOS version does not support network security checks |

## 🔐 Permissions

### Android Permissions

Add these permissions to your `AndroidManifest.xml`:

- [<span style="color: #cb3837;">`ACCESS_WIFI_STATE`</span>](https://developer.android.com/reference/android/Manifest.permission#ACCESS_WIFI_STATE)
- [<span style="color: #cb3837;">`ACCESS_NETWORK_STATE`</span>](https://developer.android.com/reference/android/Manifest.permission#ACCESS_NETWORK_STATE)
- [<span style="color: #cb3837;">`ACCESS_FINE_LOCATION`</span>](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION)

### iOS Permissions

Add these entries to your `Info.plist`:
```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Location permission is required to check Wi-Fi network security</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>Location permission is required to check Wi-Fi network security</string>
```

Additionally, enable the **Access WiFi Information** capability in your Xcode project:

1. Open your project in Xcode
2. Select your target
3. Go to **Signing & Capabilities**
4. Click **+ Capability**
5. Add **Access WiFi Information**

## ⚠️ Important Notes

> **Location Requirements:**
>
> - Location permission **must** be granted by the user
> - Location services **must** be enabled on the device
> - Without these, Wi-Fi details (SSID/BSSID) may be inaccessible
> - The API may return limited or fallback results if requirements aren't met

## 🤝 Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## 📄 License

[MIT](./LICENSE)