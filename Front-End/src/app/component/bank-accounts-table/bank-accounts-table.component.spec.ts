import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BankAccountsTableComponent } from './bank-accounts-table.component';

describe('AccountsTableComponent', () => {
  let component: BankAccountsTableComponent;
  let fixture: ComponentFixture<BankAccountsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankAccountsTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BankAccountsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
