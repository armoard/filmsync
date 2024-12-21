package com.example.demo1.exceptions.Profile;

public class AlreadyFollowingUserException extends RuntimeException {
    public AlreadyFollowingUserException(String message) {
        super(message);
    }
}
