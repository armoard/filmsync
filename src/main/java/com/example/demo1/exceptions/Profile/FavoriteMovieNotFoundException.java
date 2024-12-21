package com.example.demo1.exceptions.Profile;

public class FavoriteMovieNotFoundException extends RuntimeException {
    public FavoriteMovieNotFoundException(String message) {
        super(message);
    }
}
