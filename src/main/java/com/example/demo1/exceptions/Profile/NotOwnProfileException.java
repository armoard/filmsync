package com.example.demo1.exceptions.Profile;

public class NotOwnProfileException extends RuntimeException {
    public NotOwnProfileException(String message) {
        super(message);
    }
}
