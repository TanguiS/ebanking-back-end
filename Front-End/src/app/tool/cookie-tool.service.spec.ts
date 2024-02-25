import { TestBed } from '@angular/core/testing';

import { CookieToolService } from './cookie-tool.service';

describe('CookieToolService', () => {
  let service: CookieToolService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CookieToolService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
