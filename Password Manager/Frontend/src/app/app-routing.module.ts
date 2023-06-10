import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomePageComponent} from "./components/pages/welcome-page/welcome-page.component";
import {HomePageComponent} from "./components/pages/home-page/home-page.component";
import {CreatePasswordPageComponent} from "./components/pages/create-password-page/create-password-page.component";
import {QrPageComponent} from "./components/pages/qr-page/qr-page.component";
import {NotFoundPageComponent} from "./components/pages/not-found-page/not-found-page.component";
import {PasswordMetadataComponent} from "./components/ui-elements/password-metadata/password-metadata.component";

// TODO: make welcome open the first time the app starts else redirect to home
const routes: Routes = [
  {path: 'welcome', component: WelcomePageComponent},
  {path: 'home', component:HomePageComponent},
  {path: '',   redirectTo: '/welcome', pathMatch: 'full' },
  {path: 'mindset', redirectTo: '/welcome', pathMatch: 'full'},
  {path: 'newPassword', component: CreatePasswordPageComponent},
  {path: 'qr', component: QrPageComponent},
  {path: 'passMeta', component: PasswordMetadataComponent},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
