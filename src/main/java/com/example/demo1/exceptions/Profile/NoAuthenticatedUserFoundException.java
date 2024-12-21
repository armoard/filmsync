package com.example.demo1.exceptions.Profile;

public class NoAuthenticatedUserFoundException extends RuntimeException {
    public NoAuthenticatedUserFoundException(String message) {
        super(message);
    }
}
