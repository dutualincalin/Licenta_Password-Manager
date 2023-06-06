import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-qr-page',
  templateUrl: './qr-page.component.html',
  styleUrls: ['./qr-page.component.scss']
})
export class QrPageComponent{
  qrPath: string = "/assets/qr-home-selection.jpg";

  constructor(private router: Router) {
  }

  goHome() {
    this.router.navigate(["/home"]);
  }

  downloadQR() {
    let downloadElement=document.createElement('a');
    downloadElement.setAttribute('href',this.qrPath);
    downloadElement.setAttribute('download','');
    document.body.appendChild(downloadElement);
    downloadElement.click();
    document.body.removeChild(downloadElement);
  }

  // TODO: Initialize qrPath in this page
}
