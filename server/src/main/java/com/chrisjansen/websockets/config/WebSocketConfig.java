package com.chrisjansen.websockets.config;

import com.chrisjansen.websockets.webSocket.UserPrincipalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * Jacked from: https://www.callicoder.com/spring-boot-websocket-chat-example/
 *
 */
@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin//allow inbound requests from the Angular client!
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
      registration.interceptors(new UserPrincipalInterceptor());
  }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
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
//            }
//                      @Override
//                      protected Principal determineUser(ServerHttpRequest request,
//                                                        WebSocketHandler wsHandler,
//                                                        Map<String, Object> attributes) {
//                          return new StompPrincipal(UUID.randomUUID().toString());
//                      }
//                  }
//                  )
//                .withSockJS()
        ; //used by the client apps to initially connect.  SockJS is used to enable a fallback for browsers without websocket support!
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

    public class StompPrincipal implements Principal {
        String name;

        StompPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}

