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

getAllJobs(
  page: number = 0,
  size: number = 10,
  sort: string = 'id',
  direction: string = 'asc',
  search?: string,
  status?: string
): Observable<any> {
  let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString())
    .set('sort', sort)
    .set('direction', direction);

  if (search) {
    params = params.set('search', search);
  }

  if (status) {
    params = params.set('status', status);
  }

  return this.http.get<any>(`${this.baseUrl}/jobs/job-list`, {
    params
  });
}
getBrowseJobs(page: number = 0, size: number = 10, search?: string): Observable<any> {
  let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

  if (search && search.trim() !== '') {
    params = params.set('search', search.trim());
  }

  return this.http.get<any>(`${this.baseUrl}/jobs/browse-jobs`, { params });
}
  
}
