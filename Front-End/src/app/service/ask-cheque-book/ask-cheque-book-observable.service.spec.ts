import { TestBed } from '@angular/core/testing';

import { AskChequeBookObservableService } from './ask-cheque-book-observable.service';

describe('AskChequeBookService', () => {
  let service: AskChequeBookObservableService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AskChequeBookObservableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
