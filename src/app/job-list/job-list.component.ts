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
  allJobs: any[] = [];
  filteredJobs: any[] = [];
  paginatedJobs: any[] = [];

  searchTerm: string = '';
  currentPage: number = 1;
  pageSize: number = 5;


  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.fetchJobs();
  }

  fetchJobs(): void {
    this.jobService.getAllJobs().subscribe({
      next: (response) => {
        if (response.status) {
          this.allJobs = response.data || [];
          this.filterJobs();     } else {
          console.error('Failed to fetch jobs:', response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching jobs:', error);
      }
    });
  }
 
  filterJobs() {
    const term = this.searchTerm.toLowerCase();
    this.filteredJobs = this.allJobs.filter(job =>
      job.title?.toLowerCase().includes(term) ||
      job.company?.toLowerCase().includes(term) ||
      job.location?.toLowerCase().includes(term)
    );
    this.currentPage = 1;
    this.setPagination();
  }

  setPagination() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedJobs = this.filteredJobs.slice(start, end);
  }

  changePage(page: number) {
    this.currentPage = page;
    this.setPagination();
  }

  get totalPages(): number[] {
    return Array(Math.ceil(this.filteredJobs.length / this.pageSize)).fill(0).map((_, i) => i + 1);
  }
}
