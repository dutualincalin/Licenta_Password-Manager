import {Component, EventEmitter, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {fadeInOut} from "../../../route-animations";
import {ConfigurationService} from "../../../services/configuration.service";
import {MessageService} from "primeng/api";
import AsyncLock from "async-lock";
import {AppComponent} from "../../../app.component";
import {QrService} from "../../../services/qr.service";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.scss'],
  animations: [fadeInOut],
})
export class WelcomePageComponent implements OnInit{
  private lock = new AsyncLock({timeout:2000});

  showWelcome = false;
  loadingShow = false;
  uploading = false;

  showProgressBar = false;
  progressBarLoading: number = 0;

  startQRScanningEvent: EventEmitter<void> = new EventEmitter<void>();
  showQRDialog = false;

  showImgDialog = false;
  imgLoaded = true;
  imgFileURL: string = "";
  imgFile: File;

  constructor(
    private router: Router,
    private configurationService: ConfigurationService,
    private messageService: MessageService,
    private appComponent: AppComponent,
    private qrService: QrService,
  ) {
  }

  ngOnInit() {
    this.lock.acquire('initWelcome', () => {
      if (!this.appComponent.appStarted) {
        this.appComponent.appStarted = true;
        this.initWelcome();
      }
    },
      () => {}
    );
  }

  async initWelcome() {
    await this.sleep(1000);
    this.showWelcome = true;

    await this.sleep(1500);
    this.showWelcome = false

    await this.sleep(1000);
    this.loadingShow = true;
    this.showProgressBar = true;

    await this.sleep(1000);
    this.progressBarLoading = 20;

    await this.sleep(1000);
    this.progressBarLoading = 40;

    this.configurationService.gather().subscribe({
      next: async () => {
        this.progressBarLoading = 80;
        await this.sleep(1000);
        this.progressBarLoading = 100;
        await this.sleep(1000);
        this.goToHomePage();
      },

      error: err => {
        if (err.status == 404) {
          this.progressBarLoading = 50;
          this.loadingShow = false;
          this.imgLoaded = false;
        }

        else {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'The client can\'t connect to the server, please refresh the page or restart the app in Docker',
            sticky: true
          })
        }
      }
    });
  }

  sleep(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  onImgSelect($event: any) {
    this.imgFile = $event.target.files[0];

    if(this.imgFile) {
      this.imgFileURL = URL.createObjectURL(this.imgFile);
    }
  }

  onImgUpload() {
    this.uploading = true;
    this.progressBarLoading = 80;

    if(this.imgFile && this.imgFile.type.startsWith('image/')) {
      const reader = new FileReader();

      reader.onload = () => {
        const imageData = reader.result as string;
        const payload = {image: imageData};

        this.configurationService.setImg(payload).subscribe({
          next: () => {
            this.showImgDialog = false;
            this.uploading = false;

            this.progressBarLoading = 100;

            this.imgFileURL = "";
            URL.revokeObjectURL(this.imgFileURL);
            setTimeout(() => {this.goToHomePage()}, 1000);
          },

          error: () => {
            this.uploading = false;
            this.progressBarLoading = 50;

            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'The image failed to upload. Please try again!',
            });
          }
        });
      };

      reader.readAsDataURL(this.imgFile);
    }
  }

  onQRUpload($event: string) {
    this.uploading = true;
    this.progressBarLoading = 80;

    this.qrService.readQR($event).subscribe({
      next: () => {
        this.showQRDialog = false;
        this.uploading = false;

        this.progressBarLoading = 100;
        setTimeout(() => {this.goToHomePage()}, 1000);
      },

      error: () => {
        this.showQRDialog = false;

        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'The QR Code failed to upload. Please try again!',
          sticky: true
        });

        this.startQRScanningEvent.emit();
      }
    });
  }

  QRStart() {
    this.startQRScanningEvent.emit();
    this.showQRDialog = true;
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }

  QRCancel() {
    this.showQRDialog = false;
  }

  imgCancel() {
    this.showImgDialog = false;
  }
}
