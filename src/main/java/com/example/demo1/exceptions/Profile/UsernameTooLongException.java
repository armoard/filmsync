package com.example.demo1.exceptions.Profile;

public class UsernameTooLongException extends RuntimeException {
    public UsernameTooLongException(String message) {
        super(message);
    }
}
