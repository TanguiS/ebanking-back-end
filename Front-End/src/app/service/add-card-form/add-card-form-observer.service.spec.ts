import { TestBed } from '@angular/core/testing';

import { AddCardFormObserverService } from './add-card-form-observer.service';

describe('AddCardFormObserverService', () => {
  let service: AddCardFormObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddCardFormObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
