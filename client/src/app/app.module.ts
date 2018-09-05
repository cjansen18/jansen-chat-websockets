import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';

import {HttpClientModule} from '@angular/common/http';
import {
  MatButtonModule,
  MatCardModule,
  MatDatepickerModule,
  MatDialogModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AngularHalModule} from 'angular4-hal';

import {FormsModule} from '@angular/forms';

import {AppRoutingModule} from './/app-routing.module';
import {ChatNewComponent} from './chat-new/chat-new.component';
import {StompConfig, StompService} from "@stomp/ng2-stompjs";
import * as SockJS from 'sockjs-client';

// This is for SockJs
// Note too that index.html has an addition to get it to work with Angular 6
export function socketProvider() {
  return new SockJS('http://127.0.0.1:8080/chat');
}

const stompConfig: StompConfig = {
  // Which backend server?
  // This is for websockets
  // url: 'ws://localhost:8080/chat',
  url: socketProvider,


  // Headers
  // Typical keys: login, passcode, host
  headers: {
    // login: 'guest',
    // passcode: 'guest'
  },

  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeat_in: 0, // Typical value 0 - disabled
  heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 5000 (5 seconds)
  reconnect_delay: 5000,

  // Will log diagnostics on console
  debug: true
};

@NgModule({
  declarations: [
    AppComponent,
    ChatNewComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MatDatepickerModule,
    MatDialogModule,
    MatMenuModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule,
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSortModule,
    MatProgressSpinnerModule,
    AngularHalModule.forRoot(),
    AppRoutingModule
  ],
  providers: [
    StompService,
    {
      provide: StompConfig,
      useValue: stompConfig
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
