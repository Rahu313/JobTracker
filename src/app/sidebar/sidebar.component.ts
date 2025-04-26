import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule,CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  userRole: any
  constructor(private router: Router) {}
  ngOnInit(): void {
    if(window?.localStorage){
      this.userRole = localStorage.getItem('role'); // Replace this with a dynamic value

    }
    }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
