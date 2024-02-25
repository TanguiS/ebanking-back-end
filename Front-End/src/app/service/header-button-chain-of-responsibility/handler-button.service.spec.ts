import { TestBed } from '@angular/core/testing';

import { HandlerButtonService } from './handler-button.service';

describe('HandlerButtonService', () => {
  let service: HandlerButtonService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HandlerButtonService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
