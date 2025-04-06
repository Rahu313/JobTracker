import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  signup(payload: any): Observable<any> {
    return this.http.post('https://your-api-url.com/api/signup', payload);
  }
}
