import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomePageComponent} from "./components/pages/welcome-page/welcome-page.component";
import {HomePageComponent} from "./components/pages/home-page/home-page.component";
import {CreatePasswordPageComponent} from "./components/pages/create-password-page/create-password-page.component";
import {QrPageComponent} from "./components/pages/qr-page/qr-page.component";
import {NotFoundPageComponent} from "./components/pages/not-found-page/not-found-page.component";

const routes: Routes = [
  {path: 'home', component:HomePageComponent},
  {path: '', component: WelcomePageComponent},
  {path: 'new-password', component: CreatePasswordPageComponent},
  {path: 'qr', component: QrPageComponent},
  // {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
