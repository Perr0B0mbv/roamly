import axios from 'axios';
import { API_BASE_URL } from '../constants/api';
import { AuthResponse } from '../types';

const api = axios.create({ baseURL: API_BASE_URL });

export const authService = {
  register: async (username: string, email: string, password: string): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/auth/register', { username, email, password });
    return data;
  },

  login: async (email: string, password: string): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/auth/login', { email, password });
    return data;
  },

  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/auth/refresh', { refreshToken });
    return data;
  },
};
