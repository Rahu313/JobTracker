import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-application-list',
  standalone: true,
  imports: [NavbarComponent,RouterModule,CommonModule],
  templateUrl: './application-list.component.html',
  styleUrl: './application-list.component.scss'
})
export class ApplicationListComponent {
  applications = [
    {
      applicantName: 'John Doe',
      email: 'john@example.com',
      resumeLink: 'https://example.com/resume/john.pdf',
      skills: 'Angular, TypeScript, HTML',
      jobTitle: 'Frontend Developer',
      appliedDate: '2024-08-25'
    },
    {
      applicantName: 'Jane Smith',
      email: 'jane@example.com',
      resumeLink: 'https://example.com/resume/jane.pdf',
      skills: 'Node.js, MongoDB, Express',
      jobTitle: 'Backend Developer',
      appliedDate: '2024-08-28'
    }
  ];
}
