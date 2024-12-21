package com.example.demo1.exceptions.movie;

public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}