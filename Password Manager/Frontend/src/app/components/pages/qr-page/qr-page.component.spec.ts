import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrPageComponent } from './qr-page.component';

describe('QrPageComponent', () => {
  let component: QrPageComponent;
  let fixture: ComponentFixture<QrPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QrPageComponent]
    });
    fixture = TestBed.createComponent(QrPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
