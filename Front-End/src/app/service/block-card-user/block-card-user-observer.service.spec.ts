import { TestBed } from '@angular/core/testing';

import { BlockCardUserObserverService } from './block-card-user-observer.service';

describe('BlockCardUserService', () => {
  let service: BlockCardUserObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BlockCardUserObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
