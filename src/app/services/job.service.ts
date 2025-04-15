import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobService {
 private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}
  postJob(job: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/jobs/post-job`, job);
  }
  getAllJobs(): Observable<any> {
    return this.http.get(`${this.baseUrl}/jobs/job-list`);
  }
  
}
