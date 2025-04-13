import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserdataService {
  private userName: string='';
  constructor() { }
 

  setName(name: string): void {
    this.userName = name;
  }

  getName(): string {
    return this.userName;
  }

  clearName(): void {
    this.userName = '';
  }
}
