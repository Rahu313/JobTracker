import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-post-job',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule,NavbarComponent,RouterModule],
  templateUrl: './post-job.component.html',
  styleUrl: './post-job.component.scss'
})
export class PostJobComponent {
  jobForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.jobForm = this.fb.group({
      title: ['', Validators.required],
      company: ['', Validators.required],
      location: ['', Validators.required],
      type: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.jobForm.valid) {
      console.log('Job Posted:', this.jobForm.value);
      // Call backend API here
    }
  }
}
