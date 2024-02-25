import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeneficiaryPopupButtonComponent } from './beneficiary-popup-button.component';

describe('BeneficiaryPopupButtonComponent', () => {
  let component: BeneficiaryPopupButtonComponent;
  let fixture: ComponentFixture<BeneficiaryPopupButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BeneficiaryPopupButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BeneficiaryPopupButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
