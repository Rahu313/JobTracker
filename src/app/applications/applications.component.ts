import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-applications',
  standalone: true,
  imports: [NavbarComponent,CommonModule,RouterModule,FormsModule],
  templateUrl: './applications.component.html',
  styleUrl: './applications.component.scss'
})
export class ApplicationsComponent {
  applications = [
    {
      jobId: 1,
      jobTitle: 'Frontend Developer',
      companyName: 'TechVision Inc.',
      appliedOn: new Date('2025-04-03'),
      status: 'Under Review'
    },
    {
      jobId: 2,
      jobTitle: 'Backend Engineer',
      companyName: 'InnovateSoft',
      appliedOn: new Date('2025-04-04'),
      status: 'Accepted'
    },
    {
      jobId: 3,
      jobTitle: 'UI/UX Designer',
      companyName: 'DesignPro Studio',
      appliedOn: new Date('2025-04-05'),
      status: 'Rejected'
    }
  ];

  withdrawApplication(application: any) {
    if (confirm(`Are you sure you want to withdraw your application for ${application.jobTitle}?`)) {
      this.applications = this.applications.filter(app => app.jobId !== application.jobId);
    }
  }
}
