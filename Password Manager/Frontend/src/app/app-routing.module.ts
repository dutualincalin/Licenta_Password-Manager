import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomePageComponent} from "./components/pages/welcome-page/welcome-page.component";
import {HomePageComponent} from "./components/pages/home-page/home-page.component";
import {CreatePasswordPageComponent} from "./components/pages/create-password-page/create-password-page.component";
import {QrPageComponent} from "./components/pages/qr-page/qr-page.component";
import {NotFoundPageComponent} from "./components/pages/not-found-page/not-found-page.component";

const routes: Routes = [
  {path: 'welcome', component: WelcomePageComponent, data: {animation: 'isLeft'}},
  {path: '',   redirectTo: '/welcome', pathMatch: 'full'},
  {path: 'home', component: HomePageComponent},
  {path: 'newPassword', component: CreatePasswordPageComponent, data: {animation: 'isUp'}},
  {path: 'qr/:path', component: QrPageComponent, data: {animation: 'isRight'}},
  {path: '**', component: NotFoundPageComponent, data: {animation: 'isDown'}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
