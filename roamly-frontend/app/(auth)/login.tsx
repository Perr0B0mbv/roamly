import { View, Text, StyleSheet } from 'react-native';
import { Colors } from '../../constants/colors';

export default function LoginScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Roamly</Text>
      <Text style={styles.subtitle}>Inicia sesión</Text>
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
    fontSize: 32,
    fontWeight: 'bold',
    color: Colors.primary,
  },
  subtitle: {
    fontSize: 16,
    color: Colors.textSecondary,
    marginTop: 8,
  },
});
