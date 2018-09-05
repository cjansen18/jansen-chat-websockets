package com.chrisjansen.websockets.controllers;


import com.chrisjansen.websockets.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;

//https://stackoverflow.com/questions/45357194/simple-convertandsendtouser-where-do-i-get-a-username

//https://stackoverflow.com/questions/42327780/springwebsocketstomp-message-to-specific-session-not-user
//https://stackoverflow.com/questions/42923461/spring-boot-websockets-how-to-see-subscribers
//https://stackoverflow.com/questions/24795340/how-to-find-all-users-subscribed-to-a-topic-in-spring-websockets?lq=1

//https://stackoverflow.com/questions/43536507/spring-session-spring-web-socket-send-message-to-specific-client-based-on-ses
//https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket

@Controller
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * This doesnt seem to work
     *
     */
    @Autowired
    private SimpUserRegistry userRegistry;


    /**
     *  A message with destination /app/chat.sendMessage will be routed here
     *
     * @param chatMessage
     * @return
     */
    @MessageMapping("/chat.sendMessage") //inbound
    //@SendTo("/topic/public") //outbound
    @SendToUser("/topic/public") //outbound
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor  headerAccessor) {
        int userCount=userRegistry.getUserCount();
        ConcurrentHashMap sessionAttributeMap = (ConcurrentHashMap) headerAccessor.getSessionAttributes();
        String userName= (String) sessionAttributeMap.get("username");
        String simpleSessionId= (String) headerAccessor.getSessionId();

        return chatMessage;
    }

    /**
     * A message with destination /app/chat.addUser will be routed here
     * @param chatMessage
     * @param headerAccessor
     * @return
     */
    @MessageMapping("/chat.addUser") //inbound
    @SendTo("/topic/public") //outbound
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/errors", exception.getMessage());
        return exception.getMessage();
    }
}
