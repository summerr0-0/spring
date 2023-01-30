package com.example.spring.event;

public class UserJoinedEvent {
    private final String userId;

    private final String userName;

    public static UserJoinedEvent of(String userId, String userName) {
        return new UserJoinedEvent(userId, userName);
    }

    private UserJoinedEvent(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
