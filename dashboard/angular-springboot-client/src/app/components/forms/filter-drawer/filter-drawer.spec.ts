import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FilterDrawer } from './filter-drawer';

describe('FilterDrawer', () => {
  let component: FilterDrawer;
  let fixture: ComponentFixture<FilterDrawer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FilterDrawer],
    }).compileComponents();

    fixture = TestBed.createComponent(FilterDrawer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
