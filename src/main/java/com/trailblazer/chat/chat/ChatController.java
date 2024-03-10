package com.trailblazer.chat.chat;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Mapping message
    @MessageMapping("/chat.sendMessage") // URL to invoke the method for sending message
    @SendTo("/topic/public") // queued to this topic end point
    public ChatMessage sendMessage( @Payload ChatMessage chatMessage){ // here payload is like @RequestBody

        return chatMessage;
    }
    @MessageMapping("/chat.addUser") // URL to invoke the method
    @SendTo("/topic/public") // queued to this topic end point
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){
        // add username in web socket session
        // getSessionAttributes() returns Map. So, here we use put(K,V) method
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;

    }
}
