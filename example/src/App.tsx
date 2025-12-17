import { useEffect } from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  Platform,
  Alert,
  PermissionsAndroid,
} from 'react-native';
import Network from 'react-native-secure-network';
import { check, PERMISSIONS, request, RESULTS } from 'react-native-permissions';

export default function App() {
  useEffect(() => {
    checkLocationPermission();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const checkLocationPermission = async () => {
    const permission =
      Platform.OS === 'ios'
        ? PERMISSIONS.IOS.LOCATION_WHEN_IN_USE
        : PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION;

    const result = await check(permission);

    switch (result) {
      case RESULTS.UNAVAILABLE:
        console.log('This feature is not available on this device or OS.');
        break;
      case RESULTS.DENIED:
        console.log('Permission has not been requested / is denied.');
        handleDeniedPermissionModal();
        break;
      case RESULTS.GRANTED:
        console.log('Permission is granted.');
        break;
      case RESULTS.BLOCKED:
        console.log('Permission is denied and cannot be requested (blocked).');
        break;
    }
  };

  const handleDeniedPermissionModal = () => {
    Alert.alert(
      'Permission Required',
      'We need access to your location to provide accurate results and personalized services. Please enable location permissions in your device settings.',
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'Open Settings',
          onPress: () => {
            console.log('Open settings');
          },
        },
      ]
    );
  };

  const requestLocationPermission = async () => {
    if (Platform.OS === 'ios') {
      const status = await request(PERMISSIONS.IOS.LOCATION_WHEN_IN_USE);
      if (status === RESULTS.GRANTED) {
        console.log('Location permission granted.');
      } else {
        console.log('Location permission denied.');
        handleDeniedPermissionModal();
      }
    } else if (Platform.OS === 'android') {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('Location permission granted.');
      } else {
        console.log('Location permission denied.');
        handleDeniedPermissionModal();
      }
    }
  };

  const checkApi = async () => {
    try {
      const res = await Network.getConnectionStatus();
      console.log('final result', res);
    } catch (e) {
      console.log('error', e);
    }
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={checkApi} style={styles.background}>
        <Text>Get Status</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={requestLocationPermission}
        style={styles.background}
      >
        <Text>Ask Location permission</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  background: {
    width: '80%',
    backgroundColor: 'yellow',
    padding: 10,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 20,
    marginVertical: 10,
  },
});
