import { StyleSheet, Text, View } from 'react-native';

import * as AliyunPush from 'expo-aliyun-push';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{AliyunPush.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
