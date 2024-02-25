import { TestBed } from '@angular/core/testing';

import { BankAccountsTableObserverService } from './bank-accounts-table-observer.service';

describe('AccountTableObserverService', () => {
  let service: BankAccountsTableObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankAccountsTableObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
