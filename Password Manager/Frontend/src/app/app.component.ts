import {Component, HostListener} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {slider} from "./route-animations";
import {ConfigurationService} from "./services/configuration.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    slider,
  ]
})
export class AppComponent{
  title = 'Password Manager';
  appStarted = false;

  constructor(
    private configurationService: ConfigurationService) {
  }


  @HostListener('window:beforeunload', ['$event'])
  async handleWindowBeforeUnload(event: Event) {
    event.preventDefault();
    await this.configurationService.save().subscribe();
  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData['animation'];
  }

}
