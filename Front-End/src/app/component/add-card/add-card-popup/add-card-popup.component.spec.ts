import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCardPopupComponent } from './add-card-popup.component';

describe('AddCardComponent', () => {
  let component: AddCardPopupComponent;
  let fixture: ComponentFixture<AddCardPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCardPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCardPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
