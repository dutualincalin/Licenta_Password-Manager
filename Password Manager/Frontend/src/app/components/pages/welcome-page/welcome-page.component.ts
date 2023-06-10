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

  ngAfterViewInit(): void {
    setTimeout(() => {this.welcomeShow = true}, 0);
    setTimeout(() => {this.welcomeShow = false}, 3000);
    setTimeout(() => {this.loadingShow = true}, 4000);
  }

  constructor(private router: Router) {
  }

  goToHomePage() {
    this.router.navigate(['/home']);
  }

  // TODO: insert service to check app config
}
