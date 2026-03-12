export interface User {
  id: number;
  username: string;
  email: string;
  role: 'USER' | 'ADMIN';
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  username: string;
  email: string;
  role: string;
}

export interface ItineraryItem {
  id: number;
  title: string;
  description?: string;
  location?: string;
  googleMapsUrl?: string;
  latitude?: number;
  longitude?: number;
  orderIndex?: number;
}

export interface Itinerary {
  id: number;
  title: string;
  description?: string;
  isPublic: boolean;
  averageRating: number;
  ratingCount: number;
  createdAt: string;
  ownerUsername: string;
  items: ItineraryItem[];
}

export interface CommunityItinerary {
  id: number;
  title: string;
  description?: string;
  averageRating: number;
  ratingCount: number;
  ownerUsername: string;
  createdAt: string;
  itemCount: number;
}

export interface Comment {
  id: number;
  content: string;
  authorUsername: string;
  createdAt: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  last: boolean;
  first: boolean;
}
