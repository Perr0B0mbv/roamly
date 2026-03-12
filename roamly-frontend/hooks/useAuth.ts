import { useState, useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';
import { AuthResponse } from '../types';

const ACCESS_TOKEN_KEY = 'roamly_access_token';
const REFRESH_TOKEN_KEY = 'roamly_refresh_token';
const USER_KEY = 'roamly_user';

export function useAuth() {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadToken();
  }, []);

  const loadToken = async () => {
    try {
      const token = await SecureStore.getItemAsync(ACCESS_TOKEN_KEY);
      setAccessToken(token);
    } catch {
      setAccessToken(null);
    } finally {
      setIsLoading(false);
    }
  };

  const saveSession = async (auth: AuthResponse) => {
    await SecureStore.setItemAsync(ACCESS_TOKEN_KEY, auth.accessToken);
    await SecureStore.setItemAsync(REFRESH_TOKEN_KEY, auth.refreshToken);
    await SecureStore.setItemAsync(USER_KEY, JSON.stringify({
      username: auth.username,
      email: auth.email,
      role: auth.role,
    }));
    setAccessToken(auth.accessToken);
  };

  const clearSession = async () => {
    await SecureStore.deleteItemAsync(ACCESS_TOKEN_KEY);
    await SecureStore.deleteItemAsync(REFRESH_TOKEN_KEY);
    await SecureStore.deleteItemAsync(USER_KEY);
    setAccessToken(null);
  };

  return {
    accessToken,
    isLoading,
    isAuthenticated: !!accessToken,
    saveSession,
    clearSession,
  };
}
