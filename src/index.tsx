import { NativeModules } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-network-security' doesn't seem to be linked. Make sure: \n\n` +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const SecureNetworkModule = isTurboModuleEnabled
  ? require('./NativeSecureNetwork').default
  : NativeModules.SecureNetwork;

const SecureNetwork = SecureNetworkModule
  ? SecureNetworkModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

function getConnectionStatus(): Promise<object> {
  return SecureNetwork.getConnectionStatus();
}
const Network = {
  getConnectionStatus,
};

export default Network;
