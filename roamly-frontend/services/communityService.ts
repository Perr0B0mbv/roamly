import axios from 'axios';
import { API_BASE_URL } from '../constants/api';
import { CommunityItinerary, Comment, Itinerary, Page } from '../types';

const api = axios.create({ baseURL: API_BASE_URL });

export const setAuthToken = (token: string) => {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
};

export const communityService = {
  search: async (title?: string, page = 0, size = 10): Promise<Page<CommunityItinerary>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    if (title) params.set('title', title);
    const { data } = await api.get<Page<CommunityItinerary>>(`/itineraries/community/search?${params}`);
    return data;
  },

  clone: async (id: number): Promise<Itinerary> => {
    const { data } = await api.post<Itinerary>(`/itineraries/${id}/clone`);
    return data;
  },

  rate: async (id: number, rating: number): Promise<CommunityItinerary> => {
    const { data } = await api.post<CommunityItinerary>(`/itineraries/${id}/rate`, { rating });
    return data;
  },

  addComment: async (id: number, content: string): Promise<Comment> => {
    const { data } = await api.post<Comment>(`/itineraries/${id}/comments`, { content });
    return data;
  },

  getComments: async (id: number, page = 0, size = 10): Promise<Page<Comment>> => {
    const { data } = await api.get<Page<Comment>>(`/itineraries/${id}/comments?page=${page}&size=${size}`);
    return data;
  },
};
