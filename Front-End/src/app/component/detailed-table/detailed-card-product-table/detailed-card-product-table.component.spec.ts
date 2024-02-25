import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedCardProductTableComponent } from './detailed-card-product-table.component';

describe('DetailedCardProductTableComponent', () => {
  let component: DetailedCardProductTableComponent;
  let fixture: ComponentFixture<DetailedCardProductTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailedCardProductTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailedCardProductTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
