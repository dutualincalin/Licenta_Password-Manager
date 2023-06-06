import { Component } from '@angular/core';

@Component({
  selector: 'app-create-password-page',
  templateUrl: './create-password-page.component.html',
  styleUrls: ['./create-password-page.component.scss']
})
export class CreatePasswordPageComponent {
  website?: string;
  username?: string;
  version?: number;
  length?: number = 0;

  constructor() {
  }
}
