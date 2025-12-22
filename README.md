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
import Network from 'react-native-secure-network';
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
    // Handle success
    console.log(response);
  } catch (error) {
    // Handle errors
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
}
```

**Possible Responses:**

| Security Status | Message                                            |
| :-------------: | -------------------------------------------------- |
|     `false`     | Location permission denied                         |
|     `false`     | Location services are disabled                     |
|     `false`     | Connected to an unsecured open Wi-Fi network       |
|     `false`     | Connected to a weak security Wi-Fi network (WEP)   |
|     `false`     | Connected to a Wi-Fi network using insecure WPS    |
|     `false`     | Connected to a weak security Wi-Fi network (WPA-1) |
|     `true`      | Using mobile data or not connected to Wi-Fi        |
|     `true`      | Wi-Fi information is unavailable                   |
|     `true`      | Connected to a secure Wi-Fi network                |

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