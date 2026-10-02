import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'services' },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then((module) => module.Login),
  },
  {
    path: 'services',
    canActivate: [authGuard],
    loadComponent: () => import('./components/services/service-list').then((module) => module.ServiceList),
  },
  {
    path: 'services/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./components/services/service-detail').then((module) => module.ServiceDetail),
  },
  {
    path: 'services/:id/book',
    canActivate: [authGuard],
    loadComponent: () => import('./components/services/booking-form').then((module) => module.BookingForm),
  },
  { path: '**', redirectTo: 'services' },
];
