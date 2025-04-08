import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-applicantdashboard',
  standalone: true,
  imports: [NavbarComponent,RouterModule,CommonModule],
  templateUrl: './applicantdashboard.component.html',
  styleUrl: './applicantdashboard.component.scss'
})
export class ApplicantdashboardComponent {

}
