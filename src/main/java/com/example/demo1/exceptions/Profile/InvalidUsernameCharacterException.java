package com.example.demo1.exceptions.Profile;

public class InvalidUsernameCharacterException extends RuntimeException {
    public InvalidUsernameCharacterException(String message) {
        super(message);
    }
}
