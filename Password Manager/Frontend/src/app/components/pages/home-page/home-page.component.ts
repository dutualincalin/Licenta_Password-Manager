import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {PasswordMetadata} from "../../../objects/passwordMetadata";
import {ConfirmationService, MessageService} from "primeng/api";
import {PaginatorState} from "primeng/paginator";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent{
  passwordMetadataList: PasswordMetadata[] = [];
  filteredPasswords: PasswordMetadata[] = [];
  selectedPasswords: PasswordMetadata[] = [];

  QRSelectionState = false;
  generatePassword = false;
  noPasswordEntries = false;

  QRPath: string = "";
  master: string = "";
  searchValue: string = "";

  firstIndex: number = 0;
  lastIndex: number = 5;


  constructor(
    private router: Router,
    private confirmationService: ConfirmationService,
    protected messageService: MessageService
  ) {
    // this.passwordMetadataList.push(new PasswordMetadata("https://facebook.com", "bla0aaaaaaaaaaaaaaaa", 12, 64));
    // this.passwordMetadataList.push(new PasswordMetadata("https://website-test.com", "user_test", 0, 16));
    // this.passwordMetadataList.push(new PasswordMetadata("bla", "bla2", 0, 0));
    // this.passwordMetadataList.push(new PasswordMetadata("bla", "bla3", 0, 0));
    // this.passwordMetadataList.push(new PasswordMetadata("bla", "bla4", 0, 0));
    // this.passwordMetadataList.push(new PasswordMetadata("bla", "bla4", 0, 0));

    this.fetchPasswordMetadataList();

  }

  fetchPasswordMetadataList() {
    // TODO: service to fetch metadata

    if(this.passwordMetadataList.length == 0){
      this.noPasswordEntries = true;
      this.filteredPasswords = []
    }

    else {
      this.noPasswordEntries = false;
      this.filteredPasswords = this.passwordMetadataList.map((x) => x);
    }
  }

  extractSelectedState($event: boolean) {
    this.QRSelectionState = $event.valueOf();

    if(!this.QRSelectionState) {
      this.selectedPasswords = [];
    }
  }

  setSelectedMetadata($event: { passMeta: PasswordMetadata; selected: boolean }) {
    $event.selected ?
      this.selectedPasswords.push($event.passMeta) :
      this.selectedPasswords.splice(this.selectedPasswords.indexOf($event.passMeta), 1);
  }

  setNewPassword() {
    this.router.navigate(['/newPassword'])
  }

  // TODO: Export selected items to QR page component
  exportQR() {
    this.router.navigate(['/qr'])
  }

  exitApp() {
    // TODO: Service for exiting app
  }

  onPageChange($event: PaginatorState): void {
    if($event.first !== undefined && $event.rows) {
      this.firstIndex = $event.first;
      this.lastIndex = $event.first + $event.rows
    }
  }

  confirmGeneratePassword(passwordMetadata: PasswordMetadata): void {
    this.generatePassword = true;
    this.confirmationService.confirm({
      header: 'Generate Password',
      accept: () => {
        // TODO: Implement request to generate password
        this.messageService.add({severity: 'success', summary: 'Generating', detail: 'Password has been generated',});
        this.generatePassword = false;
      },

      reject: () => {
        this.messageService.add({severity: 'info', summary: 'Cancelled', detail: 'You have cancelled your action'});
        this.generatePassword = false;
      }
    });
  }

  confirmDeletePassword(passwordMetadata: PasswordMetadata): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the password entry?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        // TODO: Implement request to delete password entry
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Password entry has been deleted'});
      },

      reject: () => {
        this.messageService.add({ severity: 'info', summary: 'Cancelled', detail: 'You have cancelled your action'});
      }
    });
  }

  checkMaster(): boolean {
    let masterInput = document.getElementById("masterInput");

    if (this.master == "") {
      masterInput!.className = "p-inputtext p-component p-element ng-pristine ng-invalid ng-dirty ng-touched";
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Master Password is empty',
        sticky: true
      });
      return false;
    }

    else {
      masterInput!.className = "p-inputtext p-component p-element ng-pristine ng-valid ng-touched";
      return true;
    }
  }

  filterPasswords() {
    this.filteredPasswords = this.passwordMetadataList.filter(passwordMetadata => {
      if(this.searchValue != "") {
        return passwordMetadata.username.includes(this.searchValue)
          || passwordMetadata.website.includes(this.searchValue);
      }

      return true;
    })
  }

  selectAllAction() {
    this.filteredPasswords.length != this.selectedPasswords.length ?
      this.selectedPasswords = this.filteredPasswords.map((x) => x) :
      this.selectedPasswords = [];
  }
}
