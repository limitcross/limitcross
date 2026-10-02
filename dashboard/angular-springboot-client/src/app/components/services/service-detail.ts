import { afterNextRender, Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Api, ServiceSummary } from '../../services/api';

@Component({
  selector: 'app-service-detail',
  imports: [RouterLink],
  templateUrl: './service-detail.html',
  styleUrl: './service-detail.css',
})
export class ServiceDetail {
  private readonly api = inject(Api);
  private readonly route = inject(ActivatedRoute);
  readonly service = signal<ServiceSummary | null>(null);
  readonly loading = signal(true);
  readonly errorMessage = signal('');
  private readonly serviceId = this.route.snapshot.paramMap.get('id');

  constructor() {
    afterNextRender(() => this.loadService());
  }

  categoryLabel(category: string): string {
    return category.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/^./, (letter) => letter.toUpperCase());
  }

  private loadService(): void {
    if (!this.serviceId) {
      this.errorMessage.set('A service ID is required to show its details.');
      this.loading.set(false);
      return;
    }
    this.api.getService(this.serviceId).subscribe({
      next: (service) => this.service.set(service),
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.status === 404
          ? 'We could not find that service.'
          : 'Service details could not be loaded. Please try again.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
