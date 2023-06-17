import {Component, ElementRef, EventEmitter, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {PasswordConfiguration} from "../../../objects/passwordConfiguration";
import {ConfirmationService, MessageService} from "primeng/api";
import {PaginatorState} from "primeng/paginator";
import {fadeInOut} from "../../../route-animations";
import {ConfigurationService} from "../../../services/configuration.service";
import {PasswordService} from "../../../services/password.service";
import {Clipboard} from "@angular/cdk/clipboard";
import {QrService} from "../../../services/qr.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
  animations: [fadeInOut]
})
export class HomePageComponent{
  @ViewChild('masterInput') masterInput: ElementRef<HTMLInputElement>;

  passwordMetadataList: PasswordConfiguration[] = [];
  filteredPasswords: PasswordConfiguration[] = [];
  selectedPasswords: PasswordConfiguration[] = [];
  noPasswordEntries:boolean = false;
  passwordMetadataNum: number = 0;

  showQRImportDialogue = false;
  QRSelectionState = false;
  uploadingQR:boolean = false;

  showMasterPasswordDialog = false;
  showPasswordGeneratedDialog = false;

  generatedPassword: string = "";
  master: string = "";

  searchValue: string = "";
  firstIndex: number = 0;
  lastIndex: number = 5;


  protected startQRImportingEvent: EventEmitter<void> = new EventEmitter<void>();

  constructor(
    private router: Router,
    protected messageService: MessageService,
    private confirmationService: ConfirmationService,
    private configurationService: ConfigurationService,
    private passwordService: PasswordService,
    private qrService: QrService,
    private clipboard: Clipboard
  ) {
    this.fetchPasswordMetadataList();
  }

  /************************************ Password Related ********************************************************/

  setNewPassword() {
    this.router.navigate(['/newPassword'])
  }

  fetchPasswordMetadataList() {
    this.passwordService.fetch().subscribe({
      next: response=> {
        if(response.body) {
          this.passwordMetadataList = response.body.map(passwordMeta => passwordMeta);
          this.passwordMetadataNum = this.passwordMetadataList.length;
        }

        if (this.passwordMetadataNum == 0) {
          this.noPasswordEntries = true;
          this.filteredPasswords = [];
        } else {
          this.noPasswordEntries = false;
          this.filteredPasswords = this.passwordMetadataList.map((x) => x);
        }
      }
    });
  }

  confirmGeneratePassword(passwordMetadata: PasswordConfiguration): void {
    this.showMasterPasswordDialog = true;
    this.confirmationService.confirm({
      header: 'Generate Password',

      accept: () => {
        this.showPasswordGeneratedDialog = true;
        this.passwordService.generate(this.master, passwordMetadata.id).subscribe({
          next: response => {
            this.generatedPassword = response.body ? response.body["passKey"] : "";
            this.showMasterPasswordDialog = false;
            this.master = '';
          },

          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Generation Failed',
              detail: 'An error has occurred. Please try again!'
            });
            this.showMasterPasswordDialog = false;
          }
        })
      },

      reject: () => {
        this.messageService.add({severity: 'info', summary: 'Cancelled', detail: 'You have cancelled your action'});
        this.showMasterPasswordDialog = false;
      }
    });
  }

  confirmDeletePassword(passwordMetadata: PasswordConfiguration): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the password entry?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.passwordService.delete(passwordMetadata.id).subscribe({
          error: err => {
            this.messageService.add({
              severity: 'error',
              summary: 'Delete error',
              detail: err.error
            });
          },

          complete: () => {
            this.messageService.add({severity: 'success', summary: 'Deleted', detail: 'Password entry has been deleted'});
            this.fetchPasswordMetadataList();
          }
        })

      },

      reject: () => {
        this.messageService.add({severity: 'info', summary: 'Cancelled', detail: 'You have cancelled your action'});
      }
    });
  }

  setSelectedMetadata($event: { passMeta: PasswordConfiguration; selected: boolean }) {
    $event.selected ?
      this.selectedPasswords.push($event.passMeta) :
      this.selectedPasswords.splice(this.selectedPasswords.indexOf($event.passMeta), 1);
  }

  /*************************************************************************************************************/

  /******************************************** Action Related *************************************************/

  filterPasswords() {
    this.filteredPasswords = this.passwordMetadataList.filter(passwordMetadata => {
      if (this.searchValue != "") {
        return passwordMetadata.username.includes(this.searchValue)
          || passwordMetadata.website.includes(this.searchValue);
      }

      return true;
    })
  }

  checkMaster(): boolean {
    let masterInput = document.getElementById("masterInput");

    if (this.master == "") {
      this.masterInput.nativeElement.className = "p-inputtext p-component p-element ng-pristine ng-invalid ng-dirty ng-touched";
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Master Password is empty',
        sticky: true
      });
      return false;
    } else {
      this.masterInput.nativeElement.className = "p-inputtext p-component p-element ng-pristine ng-valid ng-touched";
      return true;
    }
  }

  exportQR() {
    this.qrService.exportQR(this.selectedPasswords).subscribe({
      next: response => {
        let qrPath = response ? URL.createObjectURL(response) : '';
        this.router.navigate(['/qr', qrPath], {skipLocationChange: true});
      },

      error: err => {
        this.messageService.add({
          severity: 'error',
          summary: 'Export error',
          detail: err.error
        });
      }
    });
  }

  selectAllAction() {
    this.filteredPasswords.length != this.selectedPasswords.length ?
      this.selectedPasswords = this.filteredPasswords.map((x) => x) :
      this.selectedPasswords = [];
  }

  resetGeneratedPassword() {
    this.generatedPassword = '';
  }

  copyToClipboard() {
    this.clipboard.copy(this.generatedPassword);
  }

  /*************************************************************************************************************/

  /******************************************** UI Related ****************************************************/

  extractSelectedState($event: boolean) {
    this.QRSelectionState = $event.valueOf();

    if (!this.QRSelectionState) {
      this.selectedPasswords = [];
    }
  }

  onPageChange($event: PaginatorState): void {
    if ($event.first !== undefined && $event.rows) {
      this.firstIndex = $event.first;
      this.lastIndex = $event.first + $event.rows
    }
  }

  finishPasswordGeneration() {
    this.showPasswordGeneratedDialog = false;
    this.generatedPassword = '';
  }


  startImportFromQR() {
    this.showQRImportDialogue = true;
    this.startQRImportingEvent.emit();
  }

  QRCancel() {
    this.showQRImportDialogue = false;
  }

  QRImport($event: string) {
    this.uploadingQR = true;

    this.qrService.readQR($event).subscribe({
      next: () => {
        this.showQRImportDialogue = false;
        this.uploadingQR = false;
        this.fetchPasswordMetadataList();
      },

      error: err => {
        this.uploadingQR = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error,
          sticky: true
        });
        this.startQRImportingEvent.emit();
      }
    });
  }
}
