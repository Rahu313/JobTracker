import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApplicationService } from '../services/application.service';
import { JobService } from '../services/job.service';
import { NavbarComponent } from '../navbar/navbar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apply-job',
  standalone: true,
  imports: [NavbarComponent,CommonModule,FormsModule,RouterModule,ReactiveFormsModule],
  templateUrl: './apply-job.component.html',
  styleUrl: './apply-job.component.scss'
})
export class ApplyJobComponent implements OnInit{
  applicationForm!: FormGroup<any>;

  jobs: any[] = [];
  resumeError: string | null = null;
  jobId:any;
  jobTitle:any='';
  constructor(
    private fb: FormBuilder,
private route:ActivatedRoute,
    private applicationService: ApplicationService,
    private toastrService: ToastrService,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.jobId = params.get('id');
    });
  
    
    this.route.queryParamMap.subscribe(params => {
      this.jobTitle = params.get('title');
    });
  
    
    
    this.initializeForm();

  }

  // Initialize form group with validations
  initializeForm() {
    this.applicationForm = this.fb.group({
      jobId: ['', Validators.required],
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      linkedin: ['', [Validators.required]],
      currentCtc: ['', [Validators.required]],
      expectedCtc: ['', [Validators.required]],
      resume: [null, [Validators.required]]
    });
    this.applicationForm.patchValue({
      jobId: this.jobId
    });
  }



  // Handle resume file selection
  onResumeSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.applicationForm?.get('resume')?.setValue(file);
      this.resumeError = null;
    }
  }

  // Handle form submission
  onSubmit() {
    if (this.applicationForm?.invalid) return;

    const formData = new FormData();
    Object.keys(this.applicationForm?.value).forEach(key => {
      formData.append(key, this.applicationForm?.get(key)?.value);
    });

    this.applicationService.submitApplication(formData).subscribe(
      (response) => {
        if (response.status) {
          this.toastrService.success('Application submitted successfully!', 'Success');
          this.router.navigate(['/applications']);
        } else {
          this.toastrService.error(response.message, 'Error');
        }
      },
      (error) => {
        this.toastrService.error('Something went wrong!', 'Error');
      }
    );
  }
}
