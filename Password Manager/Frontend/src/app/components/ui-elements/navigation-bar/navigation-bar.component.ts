import {Component, EventEmitter, Output} from '@angular/core';
import {fadeInOut} from "../../../route-animations";

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.scss'],
  animations: [fadeInOut]
})
export class NavigationBarComponent{
  @Output('passButtonAction') passButtonAction = new EventEmitter<void>;
  @Output('qrExportAction') qrExportAction = new EventEmitter<void>;
  @Output('qrSelectionAction') qrSelectionChange = new EventEmitter<boolean>;
  @Output('selectAllAction') selectAllEvent = new EventEmitter<void>;

  qrSelection: boolean = false;

  switchQRSelectionState() {
    this.qrSelection = !this.qrSelection;
    this.qrSelectionChange.emit(this.qrSelection);
  }

  exportQR() {
    this.switchQRSelectionState();
    this.qrExportAction.emit();
  }

  selectAll() {
    this.selectAllEvent.emit();
  }
}
