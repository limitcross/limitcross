import { Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ServiceSummary {
  id: string;
  title: string;
  emoji: string;
  category: string;
  description: string;
  priceRange: string;
  startingPrice: number;
  rating: number;
  reviewsCount: number;
  duration: string;
  highlights: string[];
  isPopular: boolean;
}

export interface LoginRequest {
  username: string;
  password: string;
  rememberMe: boolean;
}

export interface LoginResponse {
  id_token: string;
}

export interface CreateBookingRequest {
  serviceId: string;
  date: string;
  timeSlot: string;
  address: string;
}

export interface BookingResponse {
  id: string;
  serviceId: string;
  serviceTitle: string;
  emoji: string;
  price: number;
  serviceFee: number;
  totalPrice: number;
  date: string;
  timeSlot: string;
  address: string;
  status: string;
  assignedProName: string;
  proRating: number;
}

@Service()
export class Api {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  getServices(): Observable<ServiceSummary[]> {
    return this.http.get<ServiceSummary[]>(`${this.baseUrl}/services`);
  }

  getService(id: string): Observable<ServiceSummary> {
    return this.http.get<ServiceSummary>(`${this.baseUrl}/services/${encodeURIComponent(id)}`);
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/authenticate`, request);
  }

  createBooking(request: CreateBookingRequest): Observable<BookingResponse> {
    return this.http.post<BookingResponse>(`${this.baseUrl}/bookings`, request);
  }
}
