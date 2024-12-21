package com.example.demo1.exceptions.Profile;

public class MovieAlreadyFavoritedException extends RuntimeException {
    public MovieAlreadyFavoritedException(String message) {
        super(message);
    }
}
