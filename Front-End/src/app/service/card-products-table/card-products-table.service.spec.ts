import { TestBed } from '@angular/core/testing';

import { CardProductsTableService } from './card-products-table.service';

describe('CardProductsTableService', () => {
  let service: CardProductsTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardProductsTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
