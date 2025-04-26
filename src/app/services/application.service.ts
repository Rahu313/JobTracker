import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
   private baseUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }

  // Submit Application API call
  submitApplication(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/apply`, formData);
  }

}
