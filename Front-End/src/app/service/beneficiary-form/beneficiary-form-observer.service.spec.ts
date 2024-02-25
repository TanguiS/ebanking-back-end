import { TestBed } from '@angular/core/testing';

import { BeneficiaryFormObserverService } from './beneficiary-form-observer.service';

describe('BeneficiaryFormService', () => {
  let service: BeneficiaryFormObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeneficiaryFormObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
