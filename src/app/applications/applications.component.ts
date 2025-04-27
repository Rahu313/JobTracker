import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { ApplicationService } from '../services/application.service';

@Component({
  selector: 'app-applications',
  standalone: true,
  imports: [NavbarComponent,CommonModule,RouterModule,FormsModule],
  templateUrl: './applications.component.html',
  styleUrl: './applications.component.scss'
})
export class ApplicationsComponent implements OnInit {
  applications: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';
  debounceTimer: any; 
  

  constructor(private applicationService: ApplicationService) {}

  ngOnInit(): void {
    this.fetchApplications();
  }

  fetchApplications(): void {
    this.applicationService.getMyApplications(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe(
        (response) => {
          if (response.status) {
            this.applications = response.data.applications;
            this.totalPages = response.data.totalPages;
            this.totalElements = response.data.totalElements;
            this.currentPage = response.data.currentPage;
          } else {
            this.applications = [];
          }
        },
        (error) => {
          console.error('Error fetching applications', error);
        }
      );
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchApplications();
    }
  }
  onSearch() {
    this.searchTerm = this.searchTerm.trim(); 

    clearTimeout(this.debounceTimer); 
    this.debounceTimer = setTimeout(() => {
      this.fetchApplications();
    }, 500); 
  }

  // onSearch(): void {
  //   this.currentPage = 0;
  //   this.fetchApplications();
  // }
  
}
