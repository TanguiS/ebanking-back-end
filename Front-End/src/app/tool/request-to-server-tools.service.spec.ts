import { TestBed } from '@angular/core/testing';

import { RequestToServerToolsService } from './request-to-server-tools.service';

describe('RequestToServerToolsService', () => {
  let service: RequestToServerToolsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestToServerToolsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
