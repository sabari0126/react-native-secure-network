/**
 * Unique numeric response codes returned by the library to differentiate
 * between every possible network-security outcome on Android & iOS.
 *
 * Numbering scheme:
 *   7xxx – Secure / safe responses
 *   9xxx – Insecure / unknown responses
 */
export enum ResponseCode {
  /** Connected to a secure Wi-Fi network (WPA2/WPA3/Enterprise) */
  WIFI_SECURE = 7001,

  /** Device is not connected to Wi-Fi (e.g. mobile data) */
  WIFI_NOT_CONNECTED = 7002,

  /** Wi-Fi information could not be retrieved */
  WIFI_INFO_UNAVAILABLE = 7003,

  /** Network security status could not be determined */
  UNKNOWN = 7004,

  /** Connected to an open / unsecured Wi-Fi network */
  WIFI_UNSECURED_OPEN = 9001,

  /** Connected to a Wi-Fi network using weak WEP encryption (Android) */
  WIFI_UNSECURED_WEP = 9002,

  /** Connected to a Wi-Fi network using insecure WPS (Android) */
  WIFI_UNSECURED_WPS = 9003,

  /** Connected to a Wi-Fi network using weak WPA-1 encryption (Android) */
  WIFI_UNSECURED_WPA = 9004,

  /** Location permission denied by the user (Android) */
  PERMISSION_DENIED = 9005,

  /** Location services are disabled on the device */
  LOCATION_SERVICES_DISABLED = 9006,

  /** Location permission not determined / required (iOS) */
  PERMISSION_NOT_DETERMINED = 9007,

  /** The OS version does not support network security checks (iOS < 14) */
  UNSUPPORTED_OS_VERSION = 9008,
}

export interface NetworkSecurityResult {
  isSecureNetwork: boolean;
  message: string;
  /** Unique numeric code identifying the exact response scenario */
  code: ResponseCode;
}
