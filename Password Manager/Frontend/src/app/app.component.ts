import {Component, HostListener, OnInit} from '@angular/core';
import {RouterOutlet } from '@angular/router';
import {slider} from "./route-animations";
import {ConfigurationService} from "./services/configuration.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    slider,
  ]
})
export class AppComponent {
  title = 'Password Manager';
  appStarted = false;

  constructor(private configurationService: ConfigurationService, private messageService: MessageService) {
  }

  @HostListener('window:beforeunload', ['$event'])
  async handleWindowBeforeUnload(event: Event) {
    event.preventDefault();
    await this.configurationService.save().subscribe({
      next: () => {
        // TODO: Uncomment at the end
        // this.configurationService.shutdownSignal().subscribe({
        //   next: () => {
        //     window.close();
        //   }
        // });
      },

      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Failed Action',
          detail: 'The app couldn\'t exit safely. Please try again'
        });
      }
    });
  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData['animation'];
  }

}
