import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PasswordMetadata} from "../../../objects/passwordMetadata";

@Component({
  selector: 'app-password-metadata',
  templateUrl: './password-metadata.component.html',
  styleUrls: ['./password-metadata.component.scss']
})

export class PasswordMetadataComponent {
  @Input() metadata: PasswordMetadata;
  @Input() selectionEvent: boolean;
  @Input() checked: boolean;
  @Output() deleteEvent: EventEmitter<PasswordMetadata> = new EventEmitter<PasswordMetadata>();
  @Output() generateEvent: EventEmitter<PasswordMetadata> = new EventEmitter<PasswordMetadata>();
  @Output() checkboxEvent: EventEmitter<{passMeta: PasswordMetadata, selected: boolean}>
    = new EventEmitter<{passMeta: PasswordMetadata, selected: boolean}>();

  checkingEvent() {
    this.checkboxEvent.emit({passMeta: this.metadata, selected: this.checked});
  }
}
