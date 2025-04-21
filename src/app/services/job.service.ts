import { HttpClient, HttpParams } from '@angular/common/http';
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
getAllJobs(searchTerm: string, pageIndex: number, pageSize: number, sortField: string, sortDirection: string): Observable<any> {
  const params = new HttpParams()
    .set('searchTerm', searchTerm)
    .set('pageIndex', pageIndex.toString())
    .set('pageSize', pageSize.toString())
    .set('sortField', sortField)
    .set('sortDirection', sortDirection);


  return this.http.get<any>(`${this.baseUrl}/jobs/job-list`, {
    params
  });
}
  
}
