import { HttpClient, HttpParams } from '@angular/common/http';
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
    return this.http.post(`${this.baseUrl}/applications/apply`, formData);
  }
  getMyApplications(page: number, size: number, search?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<any>(`${this.baseUrl}/applications/my-applications`, { params });
  }

}
