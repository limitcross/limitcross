import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ContextDrawer } from './context-drawer';

describe('ContextDrawer', () => {
  let component: ContextDrawer;
  let fixture: ComponentFixture<ContextDrawer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContextDrawer],
    }).compileComponents();

    fixture = TestBed.createComponent(ContextDrawer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
