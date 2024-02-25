import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingPopupComponent } from './waiting-popup.component';

describe('WaitingPopupComponent', () => {
  let component: WaitingPopupComponent;
  let fixture: ComponentFixture<WaitingPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WaitingPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
