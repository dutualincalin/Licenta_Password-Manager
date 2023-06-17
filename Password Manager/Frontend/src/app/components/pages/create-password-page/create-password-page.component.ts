import {Component, ElementRef, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {PasswordService} from "../../../services/password.service";

@Component({
  selector: 'app-create-password-page',
  templateUrl: './create-password-page.component.html',
  styleUrls: ['./create-password-page.component.scss']
})
export class CreatePasswordPageComponent{
  @ViewChild('formUsername') formUsername: ElementRef<HTMLInputElement>;
  @ViewChild('formWebsite') formWebsite: ElementRef<HTMLInputElement>;
  @ViewChild('formVersion') formVersion: ElementRef<HTMLInputElement>;

  website: string = "";
  username: string = "";
  version: number = 0;
  length: number = 16;

  constructor(
    private router: Router,
    private messageService: MessageService,
    private passwordService: PasswordService) {
  }

  checkUsername(): boolean {
    let usernameRegex = new RegExp("^[A-Za-z][A-Za-z0-9_]{0,20}$");

    if(usernameRegex.test(this.username) || this.username == ""){
      this.formUsername.nativeElement.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched"
      return true;
    } else {
      this.formUsername.nativeElement.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
      return false;
    }
  }

  checkWebsite(): boolean {
    let websiteRegex=
      new RegExp(/^(http:\/\/|https:\/\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]+.[a-z]{2,3}(\/[a-z]+)*$/);

    if(websiteRegex.test(this.website)) {
      this.formWebsite.nativeElement.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched";
      return true;
    } else {
      this.formWebsite.nativeElement.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
      return false;
    }
  }

  checkVersion(): boolean {
    if (this.version != null && this.version > -1) {
      this.formVersion.nativeElement.className = "p-inputtext p-component p-element ng-valid p-filled ng-touched"
      return true;
    } else {
      this.formVersion.nativeElement.className = "p-inputtext p-component p-element ng-invalid p-filled ng-dirty ng-touched";
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
        detail: 'The Website input doesn\'t match the pattern!',
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

      error: err => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error,
          sticky: true
        });
      }
    });
  }
}
