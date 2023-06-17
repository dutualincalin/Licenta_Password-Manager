import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PasswordConfiguration} from "../../../objects/passwordConfiguration";

@Component({
  selector: 'app-password-metadata',
  templateUrl: './password-metadata.component.html',
  styleUrls: ['./password-metadata.component.scss']
})

export class PasswordMetadataComponent {
  @Input() metadata: PasswordConfiguration;
  @Input() selectionEvent: boolean;
  @Input() checked: boolean;

  @Output() deleteEvent: EventEmitter<PasswordConfiguration> = new EventEmitter<PasswordConfiguration>();
  @Output() generateEvent: EventEmitter<PasswordConfiguration> = new EventEmitter<PasswordConfiguration>();
  @Output() checkboxEvent: EventEmitter<{passMeta: PasswordConfiguration, selected: boolean}>
    = new EventEmitter<{passMeta: PasswordConfiguration, selected: boolean}>();

  checkingEvent() {
    this.checkboxEvent.emit({passMeta: this.metadata, selected: this.checked});
  }
}
