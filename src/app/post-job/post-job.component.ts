import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar/navbar.component';
import { Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { JobService } from '../services/job.service';

@Component({
  selector: 'app-post-job',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule,NavbarComponent,RouterModule],
  templateUrl: './post-job.component.html',
  styleUrl: './post-job.component.scss'
})
export class PostJobComponent {
  jobForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastrService: ToastrService,
    private jobService: JobService,
    private router: Router
  ) {
    this.jobForm = this.fb.group({
      title: ['', Validators.required],
      company: ['', Validators.required],
      location: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
      experienceRequired: [''],
      technologyStack: [''],
      salaryRange: [''],
      // status: [''],
      jobLevel: [''],
    });
  }

  onSubmit(): void {
    if (this.jobForm.valid) {
      const jobData = this.jobForm.value;

      this.jobService.postJob(jobData).subscribe(
        (response: any) => {
          if (response.status === true) {
            this.toastrService.success(response.message, 'Success');
            this.router.navigate(['/jobs']);
          } else {
            this.toastrService.error(response.message, 'Error');
          }
        },
        (error) => {
          console.error(error);
          this.toastrService.error(error.message || 'An error occurred', 'Error');
        }
      );
    }
  }
}
