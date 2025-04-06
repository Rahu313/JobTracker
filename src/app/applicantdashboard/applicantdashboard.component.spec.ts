import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicantdashboardComponent } from './applicantdashboard.component';

describe('ApplicantdashboardComponent', () => {
  let component: ApplicantdashboardComponent;
  let fixture: ComponentFixture<ApplicantdashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicantdashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ApplicantdashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
