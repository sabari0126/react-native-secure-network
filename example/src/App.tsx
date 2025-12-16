import { Text, View, StyleSheet, TouchableOpacity } from 'react-native';
import Network from 'react-native-secure-network';

export default function App() {
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
    marginTop: 10,
  },
});
