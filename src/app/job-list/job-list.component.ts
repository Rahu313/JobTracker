import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobService } from '../services/job.service';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [NavbarComponent,RouterModule,CommonModule,FormsModule],
  templateUrl: './job-list.component.html',
  styleUrl: './job-list.component.scss'
})
export class JobListComponent {
  jobs: any[] = [];
  paginatedJobs: any[] = [];
  totalPages: number = 0;
  pages: number[] = [];
  searchTerm: string = '';
  currentPage: number = 1;
  pageSize: number = 5;
  sortDirection: string = 'asc';
  sortField: string = 'title'; // default sorting by 'title'

  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.fetchJobs();
  }

  fetchJobs(): void {
    const pageIndex = this.currentPage - 1;
    this.jobService.getAllJobs( pageIndex, this.pageSize, this.sortField, this.sortDirection,this.searchTerm).subscribe({
      next: (response) => {
        if (response.status) {
          const data = response.data;
          this.jobs = data.jobs || [];
          this.totalPages = data.totalPages || 0;
          this.paginatedJobs = this.jobs;
          this.pages = Array.from({ length: this.totalPages }, (_, i) => i + 1);
        } else {
          console.error('Failed to fetch jobs:', response.message);
        }
      },
      error: (err) => {
        console.error('Error fetching jobs:', err);
      }
    });
  }

  searchJobs(): void {
    this.currentPage = 1;
    this.fetchJobs();
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.fetchJobs();
  }

  sortJobs(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.fetchJobs();
  }

  toggleStatus(job: any): void {
    job.status = job.status === 'Active' ? 'Inactive' : 'Active';
    // Here, you should call the backend API to update the job status.
  }
}
