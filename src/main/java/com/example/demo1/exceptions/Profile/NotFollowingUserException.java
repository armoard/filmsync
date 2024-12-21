package com.example.demo1.exceptions.Profile;

public class NotFollowingUserException extends RuntimeException {
    public NotFollowingUserException(String message) {
        super(message);
    }
}
