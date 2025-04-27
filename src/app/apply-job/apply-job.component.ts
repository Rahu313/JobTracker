import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApplicationService } from '../services/application.service';
import { JobService } from '../services/job.service';
import { NavbarComponent } from '../navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-apply-job',
  standalone: true,
  imports: [NavbarComponent, CommonModule, FormsModule, RouterModule, ReactiveFormsModule],
  templateUrl: './apply-job.component.html',
  styleUrl: './apply-job.component.scss'
})
export class ApplyJobComponent implements OnInit {
  applicationForm!: FormGroup<any>;
  resumeError: string | null = null;
  jobId: any;
  jobTitle: any = '';
  existingResume: File | null = null;
  resumeUrl: any = '';
  resumeMode: 'existing' | 'upload' = 'existing'; // track mode
  selectedNewResume: File | null = null; // store newly selected resume file

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private applicationService: ApplicationService,
    private toastrService: ToastrService,
    private authService: AuthService,
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
    this.fetchExistingResume();
  }

  initializeForm() {
    this.applicationForm = this.fb.group({
      jobId: ['', Validators.required],
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      linkedin: ['', [Validators.required]],
      currentCtc: ['', [Validators.required]],
      expectedCtc: ['', [Validators.required]],
      resumeFile: [null] 
    });

    this.applicationForm.patchValue({
      jobId: this.jobId
    });
  }

  fetchExistingResume() {
    this.authService.getProfile().subscribe(
      (response) => {
        if (response.status && response.data?.resumeFile) {
          this.resumeUrl = `http://localhost:8080/api/user/uploads/resumes/${response.data.resumeFile}`;
          this.urlToFile(this.resumeUrl, response.data.resumeFile).then(file => {
            this.existingResume = file;
            this.applicationForm.get('resumeFile')?.setValue(file);
          });
        }
      },
      (error) => {
        console.error('Error fetching resume', error);
      }
    );
  }

  async urlToFile(url: string, filename: string): Promise<File> {
    const response = await fetch(url);
    const blob = await response.blob();
    return new File([blob], filename, { type: blob.type });
  }

  onResumeSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedNewResume = file;
      this.applicationForm.get('resumeFile')?.setValue(file);
      this.resumeError = null;
    }
  }

  switchToUpload() {
    this.resumeMode = 'upload';
    this.selectedNewResume = null;
    this.applicationForm.get('resumeFile')?.reset();
  }

  switchToExisting() {
    if (this.existingResume) {
      this.resumeMode = 'existing';
      this.selectedNewResume = null;
      this.applicationForm.get('resumeFile')?.setValue(this.existingResume);
      this.resumeError = null;
    }
  }

  onSubmit() {
    if (this.applicationForm.invalid) {
      this.resumeError = 'Please fill all required fields.';
      return;
    }

    const formData = new FormData();
    Object.keys(this.applicationForm.value).forEach(key => {
      if (key !== 'resumeFile') {
        formData.append(key, this.applicationForm.get(key)?.value);
      }
    });

    const resumeFile = this.applicationForm.get('resumeFile')?.value;

    if (resumeFile) {
      formData.append('resumeFile', resumeFile);
    } else {
      this.resumeError = 'Resume is required.';
      return;
    }

    this.applicationService.submitApplication(formData).subscribe(
      (response) => {
        if (response.status) {
          this.toastrService.success('Application submitted successfully!', 'Success');
          this.router.navigate(['/applied-jobs']);
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
