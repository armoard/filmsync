package com.example.demo1.exceptions.Profile;

public class TooManyFavoriteMoviesException extends RuntimeException {
    public TooManyFavoriteMoviesException(String message) {
        super(message);
    }
}
