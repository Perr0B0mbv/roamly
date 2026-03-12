import axios from 'axios';
import { API_BASE_URL } from '../constants/api';
import { Itinerary, Page } from '../types';

const api = axios.create({ baseURL: API_BASE_URL });

export const setAuthToken = (token: string) => {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
};

export const itineraryService = {
  getMyItineraries: async (page = 0, size = 10): Promise<Page<Itinerary>> => {
    const { data } = await api.get<Page<Itinerary>>(`/itineraries?page=${page}&size=${size}`);
    return data;
  },

  getById: async (id: number): Promise<Itinerary> => {
    const { data } = await api.get<Itinerary>(`/itineraries/${id}`);
    return data;
  },

  create: async (payload: Partial<Itinerary>): Promise<Itinerary> => {
    const { data } = await api.post<Itinerary>('/itineraries', payload);
    return data;
  },

  update: async (id: number, payload: Partial<Itinerary>): Promise<Itinerary> => {
    const { data } = await api.put<Itinerary>(`/itineraries/${id}`, payload);
    return data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/itineraries/${id}`);
  },
};
