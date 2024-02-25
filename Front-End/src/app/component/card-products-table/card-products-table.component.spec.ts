import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardProductsTableComponent } from './card-products-table.component';

describe('CardProductTableComponent', () => {
  let component: CardProductsTableComponent;
  let fixture: ComponentFixture<CardProductsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardProductsTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardProductsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
