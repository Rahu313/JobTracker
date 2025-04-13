import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { UserdataService } from '../services/userdata.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [CommonModule, ReactiveFormsModule,RouterModule]
})
export class LoginComponent {
  fb = inject(FormBuilder);
  router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });
constructor(private authService:AuthService, private toastr:ToastrService,private userDataService:UserdataService){

}
  onSubmit() {
    if (this.loginForm.invalid) {
      this.toastr.warning('Please fill all required fields correctly');
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        if(res.status==true){
          localStorage.setItem('token', res.data.token);
          localStorage.setItem('role', res.data.user.role)
          this.toastr.success('Login successful!', 'Success');
          this.userDataService.setName(res.data.user.name);
          const role = res.data.user.role;
          if (role === 'poster') {
              this.router.navigate(['/poster-dashboard']);
          } else {
            this.router.navigate(['/applicant-dashboard']); 
          }
        } else{
          this.toastr.error(res.message,'Error');
        }
     
      },
      error: (err) => {
        this.toastr.error(err.message,'Error');
      }
    });
  }

}
