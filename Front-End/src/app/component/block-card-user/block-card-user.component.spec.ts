import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockCardUserComponent } from './block-card-user.component';

describe('BlockCardUserComponent', () => {
  let component: BlockCardUserComponent;
  let fixture: ComponentFixture<BlockCardUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlockCardUserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BlockCardUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
