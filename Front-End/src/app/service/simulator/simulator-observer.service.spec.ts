import { TestBed } from '@angular/core/testing';

import { SimulatorObserverService } from './simulator-observer.service';

describe('SimulatorObserverService', () => {
  let service: SimulatorObserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimulatorObserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
