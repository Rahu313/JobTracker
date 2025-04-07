import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-manage-profile',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule,RouterModule,NavbarComponent],
  templateUrl: './manage-profile.component.html',
  styleUrl: './manage-profile.component.scss'
})
export class ManageProfileComponent {
  profileForm!: FormGroup;
  selectedResumeFile: File | null = null;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const userData = {
      name: 'John Doe',
      email: 'john@example.com',
      role: 'applicant', // or 'poster'
      skills: 'Angular, TypeScript',
      companyName: 'Tech Corp',
      companyWebsite: 'https://techcorp.com'
    };

    this.profileForm = this.fb.group(
      {
        name: [userData.name, Validators.required],
        email: [{ value: userData.email, disabled: true }, [Validators.required, Validators.email]],
        role: [{ value: userData.role, disabled: true }],
        resumeLink: [null],
        skills: [userData.skills],
        companyName: [userData.companyName],
        companyWebsite: [userData.companyWebsite],
        newPassword: ['', [Validators.minLength(6)]],
        confirmPassword: ['']
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('newPassword')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password && confirmPassword && password !== confirmPassword
      ? { passwordMismatch: true }
      : null;
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedResumeFile = input.files[0];
    }
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;

    const formValues = this.profileForm.getRawValue();

    const formData = new FormData();
    formData.append('name', formValues.name);
    formData.append('email', formValues.email);
    formData.append('role', formValues.role);

    if (formValues.role === 'applicant') {
      formData.append('skills', formValues.skills || '');
      if (this.selectedResumeFile) {
        formData.append('resume', this.selectedResumeFile);
      }
    }

    if (formValues.role === 'poster') {
      formData.append('companyName', formValues.companyName || '');
      formData.append('companyWebsite', formValues.companyWebsite || '');
    }

    if (formValues.newPassword) {
      formData.append('newPassword', formValues.newPassword);
    }

    // Submit to your backend API
    console.log('Submitting form data:', formData);
  }
}
