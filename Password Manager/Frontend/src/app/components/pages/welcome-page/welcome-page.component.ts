import {AfterViewInit, Component} from '@angular/core';
import {Router} from "@angular/router";
import {fadeInOut} from "../../../route-animations";
import {ConfigurationService} from "../../../services/configuration.service";
import {MessageService} from "primeng/api";
import AsyncLock from "async-lock";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.scss'],
  animations: [fadeInOut],
})
export class WelcomePageComponent implements AfterViewInit{
  welcomeShow = false;
  loadingShow = false;
  uploadLoading = false;

  private lock = new AsyncLock({timeout:2000});

  showProgressBar = false;
  loadingProgress: number = 0;

  imgLoaded = true;
  imgFileURL: string = "";
  imgFile: File;

  constructor(
    private router: Router,
    private configurationService: ConfigurationService,
    private messageService: MessageService,
    private appComponent: AppComponent,
  ) {
  }

  ngAfterViewInit() {
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
    this.welcomeShow = true;

    await this.sleep(1500);
    this.welcomeShow = false

    await this.sleep(1000);
    this.loadingShow = true;
    this.showProgressBar = true;

    await this.sleep(1000);
    this.loadingProgress = 20;

    await this.sleep(1000);
    this.loadingProgress = 40;

    this.configurationService.gather().subscribe({
      next: async () => {
        this.loadingProgress = 80;
        await this.sleep(1000);
        this.loadingProgress = 100;
        await this.sleep(1000);
        this.goToHomePage();
      },

      error: err => {
        if (err.status == 400) {
          this.loadingProgress = 50;
          this.loadingShow = false;
          this.imgLoaded = false;
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
    this.uploadLoading = true;
    this.loadingProgress = 80;

    if(this.imgFile && this.imgFile.type.startsWith('image/')) {
      const reader = new FileReader();

      reader.onload = () => {
        const imageData = reader.result as string;
        const payload = {image: imageData};

        this.configurationService.setImg(payload).subscribe({
          next: () => {
            URL.revokeObjectURL(this.imgFileURL);
            this.imgFileURL = "";
            this.loadingProgress = 100;
            this.uploadLoading = false;
            setTimeout(() => {this.goToHomePage()}, 1000);
          },

          error: () => {
            this.uploadLoading = false;
            this.loadingProgress = 50;
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Something went wrong. Please try again.',
              sticky: true
            });
          }
        });
      };

      reader.readAsDataURL(this.imgFile);
    }
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }
}
