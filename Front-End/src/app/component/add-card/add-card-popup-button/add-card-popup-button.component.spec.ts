import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCardPopupButtonComponent } from './add-card-popup-button.component';

describe('AddCardPopupButtonComponent', () => {
  let component: AddCardPopupButtonComponent;
  let fixture: ComponentFixture<AddCardPopupButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCardPopupButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCardPopupButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
