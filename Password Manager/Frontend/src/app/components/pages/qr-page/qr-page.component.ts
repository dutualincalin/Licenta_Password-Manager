import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-qr-page',
  templateUrl: './qr-page.component.html',
  styleUrls: ['./qr-page.component.scss']
})
export class QrPageComponent implements OnInit{
  protected qrPath: string = "";

  constructor(private activatedRoute: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    let path;
    this.activatedRoute.paramMap.subscribe(params => {
        path = params.get('path');
        console.log(path);
    });
    path ? this.qrPath = path : this.qrPath = "";
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
}
