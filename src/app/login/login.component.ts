import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

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

  onSubmit() {
    const storedUser = localStorage.getItem('user');
    const user = storedUser ? JSON.parse(storedUser) : null;

    const { email, password } = this.loginForm.value;

    if (user && user.email === email && user.password === password) {
      alert('Login successful!');
      localStorage.setItem('isLoggedIn', 'true');
      this.router.navigate(['/dashboard']);
    } else {
      alert('Invalid credentials');
    }
  }
}
