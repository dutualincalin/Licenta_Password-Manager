import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {PasswordService} from "../../../services/password.service";

@Component({
  selector: 'app-create-password-page',
  templateUrl: './create-password-page.component.html',
  styleUrls: ['./create-password-page.component.scss']
})
export class CreatePasswordPageComponent{
  website: string = "";
  username: string = "";
  version: number = 0;
  length: number = 16;

  constructor(
    private router: Router,
    private messageService: MessageService,
    private passwordService: PasswordService) {
  }

  checkVersion(): boolean {
    let versionInput = document.getElementById("versionInput");
    if (this.version != null && this.version > -1) {
      versionInput!.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched"
      return true;
    } else {
      versionInput!.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
      return false;
    }
  }

  checkUsername(): boolean {
    let usernameInput = document.getElementById("usernameInput");
    let usernameRegex = new RegExp("^[A-Za-z][A-Za-z0-9_]{0,20}$");

    if(usernameRegex.test(this.username) || this.username == ""){
      usernameInput!.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched"
      return true;
    } else {
      usernameInput!.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
      return false;
    }
  }

  checkWebsite(): boolean {
    let websiteInput = document.getElementById("websiteInput");
    let websiteRegex=
      new RegExp(/^(http:\/\/|https:\/\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]+.[a-z]{2,3}(\/[a-z]+)*$/);

    if(websiteRegex.test(this.website)) {
      websiteInput!.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched";
      return true;
    } else {
      websiteInput!.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
      return false;
    }
  }

  goHome(){
    this.router.navigate(["/home"]);
  }

  submitForm(): void {
    let truth = true;

    if(!this.checkUsername()) {
      truth = false;
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'The Username input has more than 20 characters!',
        sticky: true
      });
    }

    if(!this.checkWebsite()) {
      truth = false;
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'The Website input didn\'t match the pattern!',
        sticky: true
      });
    }

    if(!this.checkVersion()){
      truth = false;
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'The version input is negative!',
        sticky: true
      });
    }

    if(!truth) return;

    this.passwordService.create({
      website: this.website,
      username: this.username,
      length: this.length,
      version: this.version
    }).subscribe({
      complete: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Your password configuration has been created',
        })

        this.router.navigate(["/home"]);
      },

      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'An error has occurred. Please try again!',
          sticky: true
        });
      }
    });
  }
}
