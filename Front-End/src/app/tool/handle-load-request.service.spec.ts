import { TestBed } from '@angular/core/testing';

import { HandleLoadRequestService } from './handle-load-request.service';

describe('HandleLoadRequestService', () => {
  let service: HandleLoadRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HandleLoadRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
