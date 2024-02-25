import { TestBed } from '@angular/core/testing';

import { DashboardBankerObserverService } from './dashboard-banker-observer.service';

describe('DashboardBankerObserverService', () => {
  let service: DashboardBankerObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardBankerObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
