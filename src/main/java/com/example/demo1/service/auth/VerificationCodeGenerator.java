package com.example.demo1.service.auth;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class VerificationCodeGenerator {

    public String generateCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    public LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plusMinutes(15);
    }
}