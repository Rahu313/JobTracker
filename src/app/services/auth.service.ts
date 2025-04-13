import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/login`, credentials);
  }

  signup(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/signup`, userData);
  }

  forgotPassword(email: string) {
    return this.http.post(`${this.apiUrl}/user/forgot-password`, { email });
  }
  getProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/me`);
  }
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }
  updateProfile(formData: FormData) {
    return this.http.post<any>(`${this.apiUrl}/user/update-profile`, formData);
  }
  
}
