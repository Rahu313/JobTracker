import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-posterdashboard',
  standalone: true,
  imports: [NavbarComponent,RouterModule],
  templateUrl: './posterdashboard.component.html',
  styleUrl: './posterdashboard.component.scss'
})
export class PosterdashboardComponent {

}
