import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PosterdashboardComponent } from './posterdashboard.component';

describe('PosterdashboardComponent', () => {
  let component: PosterdashboardComponent;
  let fixture: ComponentFixture<PosterdashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PosterdashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PosterdashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
