import {Component, OnDestroy, OnInit} from '@angular/core';
import {StompService} from "@stomp/ng2-stompjs";
import {Message} from "@stomp/stompjs";
import {Observable} from "rxjs/Observable";
import {Subscription} from "rxjs/Subscription";
import * as SockJS from 'sockjs-client';
import * as Stomp from '@stomp/stompjs';

@Component({
  selector: 'app-chat-new',
  templateUrl: './chat-new.component.html',
  styleUrls: ['./chat-new.component.css']
})
export class ChatNewComponent implements OnInit, OnDestroy {

  // Stream of messages
  private subscription: Subscription;
  public messages: Observable<Message>;

  // Subscription status
  public subscribed: boolean;

  // Array of historic message (bodies)
  public mq: Array<string> = [];

  // A count of messages received
  public count = 0;

  private _counter = 1;

  //legacy stuffs
  username:String=null;
  greetings: string[] = [];
  showConversation: boolean = false;
  disabled: boolean;
  messageInput:String;

  localClient:Stomp.Client;

  constructor(private _stompService: StompService) { }

  ngOnInit() {
    this.subscribed = false;

    // Store local reference to Observable
    // for use with template ( | async )

    //new - modified to permit button
   // this.connect();

    //this.localClient.connect()
  }

  //new -   public subscribe() {
  public connect() {
   //See the app.module.ts file for url configuration
    if (this.subscribed) {
      return;
    }

    // Stream of messages - these are received from the chat
    // this.messages = this._stompService.subscribe('/topic/public');
    this.messages = this._stompService.subscribe('/user/topic/public');  //sends to this sessionId

    // Subscribe a function to be run on_next message - I believe this gets run when a message comes in!
    //Connects to
    //this.subscription = this.messages.subscribe(this.on_next);
    this.subscription = this.messages.subscribe(this.on_next);

    //from legacy
    // Tell your username to the server
    this._stompService.publish("/app/chat.addUser",
      JSON.stringify({sender: this.username, type: 'JOIN'}));

    this.subscribed = true;
    this.disabled=false;
    this.showConversation=true;
  }

  /** Consume a message from the _stompService */
  public on_next = (message: Message) => {


    let messageToDisplay:string;
    var messageObj=JSON.parse(message.body);

    // if(messageObj.type === 'JOIN') {
    //   messageElement.classList.add('event-message');
    //   message.content = message.sender + ' joined!';
    // } else if (messageObj.type === 'LEAVE') {
    //   messageElement.classList.add('event-message');
    //   message.content = message.sender + ' left!';
    // } else {
    //   messageElement.classList.add('chat-message');

    // Store message in "historic messages" queue
    this.mq.push(message.body + '\n');

    // Count it
    this.count++;

    // Log it to the console
    console.log(message);
  }

  public onSendMessage() {
    const _getRandomInt = (min, max) => {
      return Math.floor(Math.random() * (max - min + 1)) + min;
    };
    this._stompService.publish('/topic/ng-demo-sub',
      `{ type: "Test Message", data: [ ${this._counter}, ${_getRandomInt(1, 100)}, ${_getRandomInt(1, 100)}] }`);

    this._counter++;
  }

  //from legacy
  sendMessage(event){

    var chatMessage = {
      sender: this.username,
      content: this.messageInput,
      type: 'CHAT'};

    this._stompService.publish("/app/chat.sendMessage",
      JSON.stringify(chatMessage));

    this.messageInput ="";
  }


  ngOnDestroy() {
    this.disconnect();
  }

  public disconnect() {
    if (!this.subscribed) {
      return;
    }

    // This will internally unsubscribe from Stomp Broker
    // There are two subscriptions - one created explicitly, the other created in the template by use of 'async'
    this.subscription.unsubscribe();
    this.subscription = null;
    this.messages = null;

    this.subscribed = false;
  }
}
