import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimulatorPopupComponent } from './simulator-popup.component';

describe('SimulatorPopupComponent', () => {
  let component: SimulatorPopupComponent;
  let fixture: ComponentFixture<SimulatorPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SimulatorPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SimulatorPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
