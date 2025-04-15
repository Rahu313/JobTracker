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
  searchTerm: string = '';
  jobs: any[] = [];

  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.fetchJobs();
  }

  fetchJobs(): void {
    this.jobService.getAllJobs().subscribe({
      next: (response) => {
        if (response.status) {
          this.jobs = response.data;
          console.log(this.jobs)
        } else {
          console.error('Failed to fetch jobs:', response.message);
        }
      },
      error: (error) => {
        console.error('Error fetching jobs:', error);
      }
    });
  }

  filteredJobs() {
    if (!this.searchTerm) {
      return this.jobs;
    }

    const lowerSearch = this.searchTerm.toLowerCase();

    return this.jobs.filter(job =>
      job.title.toLowerCase().includes(lowerSearch) ||
      job.company.toLowerCase().includes(lowerSearch) ||
      job.location.toLowerCase().includes(lowerSearch)
    );
  }
}
