import { Component } from '@angular/core';
import {PrimeIcons} from "primeng/api";
import {Location} from "@angular/common";

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.scss']
})
export class NotFoundPageComponent {
  protected readonly PrimeIcons = PrimeIcons;

  constructor(private location: Location) {
  }

  sendBack() {
    this.location.back();
  }
}
