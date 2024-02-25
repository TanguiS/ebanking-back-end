import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AskChequeBookComponent } from './ask-cheque-book.component';

describe('AskChequeBookComponent', () => {
  let component: AskChequeBookComponent;
  let fixture: ComponentFixture<AskChequeBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AskChequeBookComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AskChequeBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
