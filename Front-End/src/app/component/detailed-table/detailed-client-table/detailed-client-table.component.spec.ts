import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedClientTableComponent } from './detailed-client-table.component';

describe('DetailedClientTableComponent', () => {
  let component: DetailedClientTableComponent;
  let fixture: ComponentFixture<DetailedClientTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailedClientTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailedClientTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
