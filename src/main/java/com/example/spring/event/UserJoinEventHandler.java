package com.example.spring.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserJoinEventHandler {
    @EventListener(UserJoinedEvent.class)
    public void handle(UserJoinedEvent event) {
        System.out.println(event.getUserName() + "에게 메시지 전송");
    }
}
