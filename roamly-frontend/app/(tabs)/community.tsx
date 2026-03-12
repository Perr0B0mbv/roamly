import { View, Text, StyleSheet } from 'react-native';
import { Colors } from '../../constants/colors';

export default function CommunityScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Comunidad</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: Colors.text,
  },
});
