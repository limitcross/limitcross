import { isPlatformBrowser } from '@angular/common';
import { inject, PLATFORM_ID, Service, signal } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { Api } from './api';

const TOKEN_KEY = 'limitcross-auth-token';

@Service()
export class Auth {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly api = inject(Api);
  readonly isAuthenticated = signal(this.readToken() !== null);

  token(): string | null {
    return this.readToken();
  }

  login(username: string, password: string, rememberMe: boolean) {
    return this.api.login({ username, password, rememberMe });
  }

  storeToken(token: string, rememberMe: boolean): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(TOKEN_KEY);
      const storage = rememberMe ? localStorage : sessionStorage;
      storage.setItem(TOKEN_KEY, token);
      this.isAuthenticated.set(true);
    }
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(TOKEN_KEY);
    }
    this.isAuthenticated.set(false);
  }

  private readToken(): string | null {
    return isPlatformBrowser(this.platformId)
      ? sessionStorage.getItem(TOKEN_KEY) ?? localStorage.getItem(TOKEN_KEY)
      : null;
  }
}

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(Auth).token();
  if (!token || request.url.endsWith('/authenticate')) {
    return next(request);
  }

  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
