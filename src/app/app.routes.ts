import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ApplicantdashboardComponent } from './applicantdashboard/applicantdashboard.component';
import { PosterdashboardComponent } from './posterdashboard/posterdashboard.component';
import { PostJobComponent } from './post-job/post-job.component';

export const routes: Routes = [{ path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'signup', component: SignUpComponent },
    { path: 'login', component: LoginComponent },
    { path: 'applicant-dashboard', component: ApplicantdashboardComponent },
    { path: 'poster-dashboard', component: PosterdashboardComponent },
    { path: 'post-job', component: PostJobComponent },
];
