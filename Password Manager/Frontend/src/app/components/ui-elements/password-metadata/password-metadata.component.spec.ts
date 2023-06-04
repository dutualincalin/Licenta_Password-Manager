import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordMetadataComponent } from './password-metadata.component';

describe('PasswordMetadataComponent', () => {
  let component: PasswordMetadataComponent;
  let fixture: ComponentFixture<PasswordMetadataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasswordMetadataComponent]
    });
    fixture = TestBed.createComponent(PasswordMetadataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
