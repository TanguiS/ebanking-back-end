import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassicPopupComponent } from './classic-popup.component';

describe('ErrorPopupComponent', () => {
  let component: ClassicPopupComponent;
  let fixture: ComponentFixture<ClassicPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassicPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClassicPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
