import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ApplicantdashboardComponent } from './applicantdashboard/applicantdashboard.component';
import { PosterdashboardComponent } from './posterdashboard/posterdashboard.component';
import { PostJobComponent } from './post-job/post-job.component';
import { ManageProfileComponent } from './manage-profile/manage-profile.component';
import { JobListComponent } from './job-list/job-list.component';
import { ApplicationListComponent } from './application-list/application-list.component';

export const routes: Routes = [{ path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'signup', component: SignUpComponent },
    { path: 'login', component: LoginComponent },
    { path: 'applicant-dashboard', component: ApplicantdashboardComponent },
    { path: 'poster-dashboard', component: PosterdashboardComponent },
    { path: 'post-job', component: PostJobComponent },
    { path: 'manage-profile', component: ManageProfileComponent },
    { path: 'jobs', component: JobListComponent },
    { path: 'applications', component: ApplicationListComponent },
];
