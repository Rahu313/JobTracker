import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-browse-jobs',
  standalone: true,
  imports: [NavbarComponent,CommonModule,RouterModule,FormsModule],
  templateUrl: './browse-jobs.component.html',
  styleUrl: './browse-jobs.component.scss'
})
export class BrowseJobsComponent {
  searchTerm = '';

  jobs = [
    {
      title: 'Frontend Developer',
      company: 'TechNova Inc.',
      description: 'Work on cutting-edge web applications using Angular.',
      location: 'New York, NY',
      postedDate: new Date('2025-03-01'),
    },
    {
      title: 'Backend Engineer',
      company: 'CodeCraft Labs',
      description: 'Develop scalable APIs with Node.js and Express.',
      location: 'San Francisco, CA',
      postedDate: new Date('2025-03-28'),
    },
    {
      title: 'UI/UX Designer',
      company: 'DesignMinds',
      description: 'Create intuitive and beautiful user experiences.',
      location: 'Remote',
      postedDate: new Date('2025-03-20'),
    },
  ];

  filteredJobs() {
    if (!this.searchTerm.trim()) return this.jobs;

    const lowerTerm = this.searchTerm.toLowerCase();
    return this.jobs.filter(
      (job) =>
        job.title.toLowerCase().includes(lowerTerm) ||
        job.company.toLowerCase().includes(lowerTerm)
    );
  }
}
