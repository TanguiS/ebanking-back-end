import { TestBed } from '@angular/core/testing';

import { RegistrationFormObserverService } from './registration-form-observer.service';

describe('RegistrationFormObserverService', () => {
  let service: RegistrationFormObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistrationFormObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
