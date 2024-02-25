import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedAccountTableComponent } from './detailed-account-table.component';

describe('DetailedAccountTableComponent', () => {
  let component: DetailedAccountTableComponent;
  let fixture: ComponentFixture<DetailedAccountTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailedAccountTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailedAccountTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
