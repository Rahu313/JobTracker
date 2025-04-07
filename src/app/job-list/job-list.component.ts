import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [NavbarComponent,RouterModule,CommonModule,FormsModule],
  templateUrl: './job-list.component.html',
  styleUrl: './job-list.component.scss'
})
export class JobListComponent {
  searchTerm: string = '';

  jobs = [
    {
      title: 'Frontend Developer',
      company: 'TechNova',
      location: 'New York',
      postedDate: new Date()
    },
    {
      title: 'Backend Developer',
      company: 'CodeCraft',
      location: 'San Francisco',
      postedDate: new Date()
    },
    {
      title: 'Full Stack Engineer',
      company: 'DevSolutions',
      location: 'Remote',
      postedDate: new Date()
    }
  ];

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
