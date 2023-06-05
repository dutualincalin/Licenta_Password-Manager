import {AfterViewInit, Component} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Router} from "@angular/router";

const fadeInOut = trigger('fadeInOut', [
  state('shown', style({opacity: 1})),
  state('hidden', style({opacity: 0})),
  transition('hidden => shown', [animate('1s ease-in')]),
  transition('shown => hidden', [animate('1s ease-out')]),
]);

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.scss'],
  animations: [fadeInOut],
})
export class WelcomePageComponent implements AfterViewInit{
  welcomeShow = false;
  loadingShow = false;
  loadingProgress: number = 10;

  // TODO: maybe solve this
  async ngAfterViewInit(): Promise<void> {
    this.welcomeShow = true;
    await this.myDelay(3000);
    this.welcomeShow = false;
    await this.myDelay(1000);
    this.loadingShow = true;
  }

  constructor(private router: Router) {
  }

  myDelay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }

  setLoadingProgress (newProgress: number) {
    this.loadingProgress = newProgress;

    let progressBar = document.getElementById("progress-bar");
    if (progressBar) {
      progressBar.setAttribute("style", this.loadingProgress.toString() + '%');
    }
  }

  // insert service to check app config
}
