import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardBankerComponent } from './dashboard-banker.component';

describe('DashboardBankerComponent', () => {
  let component: DashboardBankerComponent;
  let fixture: ComponentFixture<DashboardBankerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardBankerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardBankerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
