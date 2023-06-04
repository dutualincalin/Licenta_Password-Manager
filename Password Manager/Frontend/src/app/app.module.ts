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
import { PasswordFormComponent } from './components/ui-elements/password-form/password-form.component';

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
    PasswordFormComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
