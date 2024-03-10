package com.trailblazer.chat.config;


import com.trailblazer.chat.chat.ChatMessage;
import com.trailblazer.chat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j // For Logging info ( to note User joining and leaving)
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;


    /***
     * @use-case: Disconnection of a User will be notified for all connected users
     * @param event to listen every events
     */
    @EventListener
    public void handleWebsocketDisconnectListener( SessionDisconnectEvent event){
        // to be implemented
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // typecasting is done here because .get(K) returns OBJ -> (String) object
        String userName = (String) headerAccessor.getSessionAttributes().get("username");
        if (userName != null) {
            // !!!---Read Slf4j Logging in Details do some practices with it---!!!
            log.info("User disconnected: {}", userName);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(userName)
                    .build();
            // convert the message and send to all other users
            // !!!---here convertAndSend() uses METHOD OVERLOADING concept---!!!
            messageTemplate.convertAndSend("/topic/public",chatMessage);

        }

    }

}
