import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivityTimeline } from './activity-timeline';

describe('ActivityTimeline', () => {
  let component: ActivityTimeline;
  let fixture: ComponentFixture<ActivityTimeline>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivityTimeline],
    }).compileComponents();

    fixture = TestBed.createComponent(ActivityTimeline);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
