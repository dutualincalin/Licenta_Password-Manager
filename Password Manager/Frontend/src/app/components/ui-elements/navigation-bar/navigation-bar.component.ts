import {Component, EventEmitter, Output} from '@angular/core';
import {fadeInOut} from "../../../route-animations";
import {MenuItem, PrimeIcons} from "primeng/api";

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
  @Output('qrImportAction') qrImportAction = new EventEmitter<void>;

  protected readonly PrimeIcons = PrimeIcons;

  qrSelectionState: boolean = false;
  items: MenuItem[] = [
    {
      label: "Export to QR",
      icon: PrimeIcons.FILE_EXPORT,
      command: () => this.switchQRSelectionState()
    },

    {
      label: "Import from QR",
      icon: PrimeIcons.FILE_IMPORT,
      command: () => this.qrImportAction.emit()
    }
  ];

  switchQRSelectionState() {
    this.qrSelectionState = !this.qrSelectionState;
    this.qrSelectionChange.emit(this.qrSelectionState);
  }

  exportQR() {
    this.qrExportAction.emit();
    this.switchQRSelectionState();
  }

  selectAll() {
    this.selectAllEvent.emit();
  }
}
