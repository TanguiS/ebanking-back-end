import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavigationButtonsListComponent } from './navigation-buttons-list.component';

describe('NavigationButtonsListComponent', () => {
  let component: NavigationButtonsListComponent;
  let fixture: ComponentFixture<NavigationButtonsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavigationButtonsListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavigationButtonsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
