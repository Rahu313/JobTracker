import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserdataService } from '../services/userdata.service';

@Component({
  selector: 'app-applicantdashboard',
  standalone: true,
  imports: [NavbarComponent,RouterModule,CommonModule],
  templateUrl: './applicantdashboard.component.html',
  styleUrl: './applicantdashboard.component.scss'
})
export class ApplicantdashboardComponent {
  userName:string='';
  constructor(private userDataService:UserdataService){
    this.userName = this.userDataService.getName();
  }
   
}
