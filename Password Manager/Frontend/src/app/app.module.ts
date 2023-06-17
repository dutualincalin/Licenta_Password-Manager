import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomePageComponent } from './components/pages/welcome-page/welcome-page.component';
import { NavigationBarComponent } from './components/ui-elements/navigation-bar/navigation-bar.component';
import { PasswordMetadataComponent } from './components/ui-elements/password-metadata/password-metadata.component';
import { HomePageComponent } from './components/pages/home-page/home-page.component';
import { CreatePasswordPageComponent } from './components/pages/create-password-page/create-password-page.component';
import { QrPageComponent } from './components/pages/qr-page/qr-page.component';
import { NotFoundPageComponent } from './components/pages/not-found-page/not-found-page.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgOptimizedImage} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {TooltipModule} from "primeng/tooltip";
import {PanelModule} from "primeng/panel";
import {DividerModule} from "primeng/divider";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {SliderModule} from "primeng/slider";
import {MatSliderModule} from '@angular/material/slider';
import {KeyFilterModule} from "primeng/keyfilter";
import {MenubarModule} from "primeng/menubar";
import {FieldsetModule} from "primeng/fieldset";
import {ToastModule} from "primeng/toast";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ConfirmationService, MessageService} from "primeng/api";
import {StyleClassModule} from "primeng/styleclass";
import {PaginatorModule} from "primeng/paginator";
import {CheckboxModule} from "primeng/checkbox";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import { CookieService } from 'ngx-cookie-service';
import {InterceptorService} from "./services/interceptor.service";
import {DialogModule} from "primeng/dialog";
import {RippleModule} from "primeng/ripple";
import { QrScannerComponent } from './components/ui-elements/qr-scanner/qr-scanner.component';
import { NgxScannerQrcodeModule } from 'ngx-scanner-qrcode';
import {MenuModule} from "primeng/menu";
import { ImageUploaderComponent } from './components/ui-elements/image-uploader/image-uploader.component';

@NgModule({
  declarations: [
    AppComponent,
    WelcomePageComponent,
    NavigationBarComponent,
    PasswordMetadataComponent,
    HomePageComponent,
    CreatePasswordPageComponent,
    QrPageComponent,
    NotFoundPageComponent,
    QrScannerComponent,
    ImageUploaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NgOptimizedImage,
    ButtonModule,
    CardModule,
    TooltipModule,
    PanelModule,
    DividerModule,
    InputTextModule,
    FormsModule,
    SliderModule,
    MatSliderModule,
    KeyFilterModule,
    MenubarModule,
    FieldsetModule,
    ToastModule,
    ConfirmDialogModule,
    StyleClassModule,
    PaginatorModule,
    CheckboxModule,
    OverlayPanelModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN'
    }),
    DialogModule,
    RippleModule,
    MenuModule,
    NgxScannerQrcodeModule
  ],
  providers: [
    ConfirmationService,
    MessageService,
    CookieService,
    { provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
