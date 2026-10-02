import { afterNextRender, Component, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { Api, ServiceSummary } from '../../services/api';

@Component({
  selector: 'app-service-list',
  imports: [RouterLink],
  templateUrl: './service-list.html',
  styleUrl: './service-list.css',
})
export class ServiceList {
  private readonly api = inject(Api);
  readonly services = signal<ServiceSummary[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal('');
  readonly searchText = signal('');
  readonly selectedCategory = signal('all');
  readonly categories = computed(() => [...new Set(this.services().map((service) => service.category))]);
  readonly filteredServices = computed(() => {
    const query = this.searchText().trim().toLowerCase();
    const category = this.selectedCategory();
    return this.services().filter((service) => {
      const matchesCategory = category === 'all' || service.category === category;
      const matchesQuery = !query || `${service.title} ${service.description} ${service.category}`.toLowerCase().includes(query);
      return matchesCategory && matchesQuery;
    });
  });

  constructor() {
    afterNextRender(() => this.loadServices());
  }

  updateSearch(value: string): void {
    this.searchText.set(value);
  }

  categoryLabel(category: string): string {
    return category.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/^./, (letter) => letter.toUpperCase());
  }

  private loadServices(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.api.getServices().subscribe({
      next: (services) => this.services.set(services),
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(error.status === 401
          ? 'Your sign-in has expired. Please sign in again to browse services.'
          : 'Services could not be loaded. Check your connection and try again.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
