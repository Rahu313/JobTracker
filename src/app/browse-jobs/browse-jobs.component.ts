import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { JobService } from '../services/job.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-browse-jobs',
  standalone: true,
  imports: [NavbarComponent,CommonModule,RouterModule,FormsModule],
  templateUrl: './browse-jobs.component.html',
  styleUrl: './browse-jobs.component.scss'
})
export class BrowseJobsComponent {
  searchTerm: string = '';
  jobs: any[] = [];
  totalPages = 0;
  currentPage = 0;
  pageSize = 6;
  debounceTimer: any; 
  
  constructor(private jobService: JobService,private toastrService:ToastrService, private router:Router) {}
  
  ngOnInit() {
    this.loadJobs(0);
  }
  
  loadJobs(page: number) {
    this.jobService.getBrowseJobs(page, this.pageSize, this.searchTerm).subscribe((res) => {
      if (res.status) {
        this.toastrService.success(res.message,'Success')
        this.jobs = res.data.jobs;
        this.totalPages = res.data.totalPages;
        this.currentPage = res.data.currentPage;
      }
      else{
        this.toastrService.error(res.message,'Error')
      }
    });
  }
  
  filteredJobs() {
    return this.jobs;
  }
  onSearchChange() {
    this.searchTerm = this.searchTerm.trim(); 

    clearTimeout(this.debounceTimer); 
    this.debounceTimer = setTimeout(() => {
      this.loadJobs(0);
    }, 500); 
  }
  navigateToApplyJobs(id:any, title:any){
    console.log(id,title)
    this.router.navigate(['/apply-job', id], { queryParams: { title:title } });

  }
  
}
