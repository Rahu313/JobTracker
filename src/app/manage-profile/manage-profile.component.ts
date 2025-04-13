import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';

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
  userData:any;
  constructor(private fb: FormBuilder,private authService:AuthService,private toastrService:ToastrService) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      name: [''],
      email: [''],
      role: [''],
      resumeLink: [null],
      skills: [''],
      companyName: [''],
      companyWebsite: [''],
      newPassword: [''],
      confirmPassword: ['']
    }, { validators: this.passwordMatchValidator });
    
    this.authService.getProfile().subscribe((res:any)=>{
      console.log(res)
      if(res.status==true){
        // this.toastrService.error('Error',res.message)
        this.userData=res.data;
        this.initFormGroup();
      }
      else{
        this.toastrService.error('Error',res.message)
      }
     
          },err=>{
            this.toastrService.error('Error',err.message)
          })

    
  }
  initFormGroup(){
    this.profileForm = this.fb.group(
      {
        name: [this.userData.name, Validators.required],
        email: [{ value: this.userData.email, disabled: true }, [Validators.required, Validators.email]],
        role: [{ value: this.userData.role, disabled: true }],
        resumeLink: [null],
        skills: [this.userData.skills],
        companyName: [this.userData.companyName],
        companyWebsite: [this.userData.companyWebsite],
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

    formData.append('userId', this.userData.id); 
    formData.append('name', formValues.name);
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

    this.authService.updateProfile(formData).subscribe(
      (res: any) => {
        if (res.status === true) {
          this.toastrService.success('Success', res.message);
          this.ngOnInit(); // refresh user data
        } else {
          this.toastrService.error('Error', res.message);
        }
      },
      (err) => this.toastrService.error('Error', err.message)
    );
  }
}
