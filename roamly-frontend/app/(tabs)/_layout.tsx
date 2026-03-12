import { Tabs } from 'expo-router';
import { Colors } from '../../constants/colors';

export default function TabsLayout() {
  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors.primary,
        tabBarInactiveTintColor: Colors.textSecondary,
        tabBarStyle: {
          backgroundColor: Colors.tabBar,
          borderTopColor: Colors.tabBarBorder,
        },
        headerShown: false,
      }}
    >
      <Tabs.Screen name="my-trips" options={{ title: 'Mis viajes' }} />
      <Tabs.Screen name="community" options={{ title: 'Comunidad' }} />
    </Tabs>
  );
}
