import { TestBed } from '@angular/core/testing';

import { LoginFormObserverService } from './login-form-observer.service';

describe('LoginFormObserverService', () => {
  let service: LoginFormObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginFormObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
