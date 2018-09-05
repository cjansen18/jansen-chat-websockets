package com.chrisjansen.websockets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
/**
 * Jacked from: https://www.callicoder.com/spring-boot-websocket-chat-example/
 *
 */
@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin//allow inbound requests from the Angular client!
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

    @Override
    protected void configureStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/chat").setAllowedOrigins("*")
//                  .setHandshakeHandler(new DefaultHandshakeHandler() {
//
//            public boolean beforeHandshake(
//                    ServerHttpRequest request,
//                    ServerHttpResponse response,
//                    WebSocketHandler wsHandler,
//                    Map attributes) throws Exception {
//
//                if (request instanceof ServletServerHttpRequest) {
//                    ServletServerHttpRequest servletRequest
//                            = (ServletServerHttpRequest) request;
//                    HttpSession session = servletRequest
//                            .getServletRequest().getSession();
//                    attributes.put("sessionId", session.getId());
//                }
//                return true;
//            }})
                .withSockJS(); //used by the client apps to initially connect.  SockJS is used to enable a fallback for browsers without websocket support!
    }

    /**
     * Setup the Message Broker
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");  //route messages to /app to Spring app message handling methods (see ChatController)
        registry.enableSimpleBroker("/topic/", "/queue/"); //route messages to /topic to the in-memory Message Broker
    }
}