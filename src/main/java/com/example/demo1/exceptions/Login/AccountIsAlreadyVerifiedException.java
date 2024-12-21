package com.example.demo1.exceptions.Login;

public class AccountIsAlreadyVerifiedException extends RuntimeException {
    public AccountIsAlreadyVerifiedException(String message) {
        super(message);
    }
}
