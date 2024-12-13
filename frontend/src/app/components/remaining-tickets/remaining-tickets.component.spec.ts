import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemainingTicketsComponent } from './remaining-tickets.component';

describe('RemainingTicketsComponent', () => {
  let component: RemainingTicketsComponent;
  let fixture: ComponentFixture<RemainingTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemainingTicketsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemainingTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
