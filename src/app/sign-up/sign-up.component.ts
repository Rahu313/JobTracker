
import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [FormsModule,
    ReactiveFormsModule,
  CommonModule,
RouterModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})
export class SignUpComponent {
  signupForm!: FormGroup;
  resumeFile: File | null = null;
  resumeError: string = '';

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.signupForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      role: ['', Validators.required],
      skills: [''],
      companyName: [''],
      companyWebsite: ['']
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: AbstractControl): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  onRoleChange(): void {
    const role = this.signupForm.get('role')?.value;

    // Reset role-specific fields
    if (role === 'applicant') {
      this.signupForm.get('skills')?.setValidators([Validators.required]);
      this.signupForm.get('companyName')?.clearValidators();
      this.signupForm.get('companyWebsite')?.clearValidators();
    } else {
      this.signupForm.get('skills')?.clearValidators();
      this.signupForm.get('companyName')?.setValidators([Validators.required]);
      this.signupForm.get('companyWebsite')?.setValidators([Validators.required]);
    }

    this.signupForm.get('skills')?.updateValueAndValidity();
    this.signupForm.get('companyName')?.updateValueAndValidity();
    this.signupForm.get('companyWebsite')?.updateValueAndValidity();
  }

  onResumeSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
      if (!allowedTypes.includes(file.type)) {
        this.resumeError = 'Only PDF or Word documents are allowed.';
        this.resumeFile = null;
      } else {
        this.resumeError = '';
        this.resumeFile = file;
      }
    }
  }

  onSubmit(): void {
    if (this.signupForm.invalid) return;

    const formData = new FormData();
    formData.append('name', this.signupForm.get('name')?.value);
    formData.append('email', this.signupForm.get('email')?.value);
    formData.append('password', this.signupForm.get('password')?.value);
    formData.append('role', this.signupForm.get('role')?.value);

    if (this.signupForm.get('role')?.value === 'applicant') {
      formData.append('skills', this.signupForm.get('skills')?.value);
      if (this.resumeFile) {
        formData.append('resume', this.resumeFile);
      }
    } else {
      formData.append('companyName', this.signupForm.get('companyName')?.value);
      formData.append('companyWebsite', this.signupForm.get('companyWebsite')?.value);
    }

    // Replace with your actual API endpoint
    this.http.post('YOUR_BACKEND_API/signup', formData).subscribe(
      (      res: any) => console.log('Signup successful', res),
      (      err: any) => console.error('Signup failed', err)
    );
  }
}
