import { afterNextRender, Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Api, BookingResponse, ServiceSummary } from '../../services/api';

@Component({
  selector: 'app-booking-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './booking-form.html',
  styleUrl: './booking-form.css',
})
export class BookingForm {
  private readonly api = inject(Api);
  private readonly formBuilder = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  readonly service = signal<ServiceSummary | null>(null);
  readonly booking = signal<BookingResponse | null>(null);
  readonly loading = signal(true);
  readonly submitting = signal(false);
  readonly errorMessage = signal('');
  readonly minimumDate = this.tomorrow();
  readonly timeSlots = ['09:00 AM - 10:00 AM', '10:00 AM - 11:00 AM', '11:00 AM - 12:00 PM', '12:00 PM - 01:00 PM', '02:00 PM - 03:00 PM', '03:00 PM - 04:00 PM', '04:00 PM - 05:00 PM'];
  readonly form = this.formBuilder.nonNullable.group({
    date: ['', Validators.required],
    timeSlot: ['', Validators.required],
    address: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
  });
  private readonly serviceId = this.route.snapshot.paramMap.get('id');

  constructor() {
    afterNextRender(() => this.loadService());
  }

  submit(): void {
    this.errorMessage.set('');
    const { date, timeSlot, address } = this.form.getRawValue();
    if (date && date <= this.today()) {
      this.form.controls.date.setErrors({ future: true });
    }
    if (this.form.invalid || !this.serviceId) {
      this.form.markAllAsTouched();
      if (!this.serviceId) {
        this.errorMessage.set('A service must be selected before making a booking.');
      }
      return;
    }

    this.submitting.set(true);
    this.api.createBooking({ serviceId: this.serviceId, date, timeSlot, address: address.trim() }).subscribe({
      next: (booking) => this.booking.set(booking),
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.status === 401
          ? 'Your sign-in has expired. Sign in again before placing your booking.'
          : error.status === 409
            ? 'This service cannot be booked right now. Please choose another service or try again later.'
            : 'We could not place your booking. Please review your details and try again.');
        this.submitting.set(false);
      },
      complete: () => this.submitting.set(false),
    });
  }

  private loadService(): void {
    if (!this.serviceId) {
      this.errorMessage.set('A service must be selected before making a booking.');
      this.loading.set(false);
      return;
    }
    this.api.getService(this.serviceId).subscribe({
      next: (service) => this.service.set(service),
      error: () => {
        this.errorMessage.set('Service information could not be loaded. Please return to the service list and try again.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  private today(): string {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private tomorrow(): string {
    const date = new Date();
    date.setDate(date.getDate() + 1);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
