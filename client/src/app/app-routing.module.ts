import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ChatNewComponent} from "./chat-new/chat-new.component";

// See also: app.component.html
const routes: Routes = [
  { path: '', redirectTo: '/chat', pathMatch: 'full' },
  { path: 'chat', component: ChatNewComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
