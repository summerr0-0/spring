package com.example.spring.event;

import com.example.spring.event.config.Events;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/join")
    public void join() {
        System.out.println("유저 가입");
        Events.raise(UserJoinedEvent.of("userId", "김포도"));
    }
}
