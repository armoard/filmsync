package com.example.demo1.controller.auth;

import com.example.demo1.dto.login.LoginUserRequest;
import com.example.demo1.dto.login.RegisterUserRequest;
import com.example.demo1.dto.login.VerifyUserRequest;
import com.example.demo1.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    public AuthenticationController( AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody  RegisterUserRequest registerUserRequest) {
        authenticationService.signup(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Please verify your email.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@Valid @RequestBody  LoginUserRequest loginUserDto) {
        authenticationService.authenticate(loginUserDto);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody  VerifyUserRequest verifyUserRequest) {
        authenticationService.verifyUser(verifyUserRequest);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }



}
