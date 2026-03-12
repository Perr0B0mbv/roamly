import { Tabs } from 'expo-router';
import { Colors } from '../../constants/colors';

export default function TabsLayout() {
  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarStyle: {
          backgroundColor: Colors.tabBar,
          borderTopColor: Colors.tabBarBorder,
        },
        tabBarActiveTintColor: Colors.primary,
        tabBarInactiveTintColor: Colors.textSecondary,
      }}
    >
      <Tabs.Screen name="my-trips" options={{ title: 'Mis Viajes', tabBarIcon: () => null }} />
      <Tabs.Screen name="community" options={{ title: 'Comunidad', tabBarIcon: () => null }} />
    </Tabs>
  );
}
