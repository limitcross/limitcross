import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Api } from './api';
import { environment } from '../../environments/environment';

describe('Api', () => {
  let service: Api;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(Api);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    http.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('loads the catalog from the service API', () => {
    service.getServices().subscribe();

    const request = http.expectOne(`${environment.apiUrl}/services`);
    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('loads a service using its required path ID', () => {
    service.getService('hm-ac').subscribe();

    const request = http.expectOne(`${environment.apiUrl}/services/hm-ac`);
    expect(request.request.method).toBe('GET');
    request.flush({ id: 'hm-ac' });
  });

  it('submits bookings with the backend request shape', () => {
    const bookingRequest = {
      serviceId: 'hm-ac',
      date: '2026-10-05',
      timeSlot: '09:00 AM - 10:00 AM',
      address: '14 Garden Road, Pune',
    };
    service.createBooking(bookingRequest).subscribe();

    const request = http.expectOne(`${environment.apiUrl}/bookings`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(bookingRequest);
    request.flush({ id: 'booking-1' });
  });
});
