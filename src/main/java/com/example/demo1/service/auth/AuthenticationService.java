package com.example.demo1.service.auth;

import com.example.demo1.dto.login.LoginUserRequest;
import com.example.demo1.dto.login.RegisterUserRequest;
import com.example.demo1.dto.login.VerifyUserRequest;
import com.example.demo1.exceptions.Login.*;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.entity.User;
import com.example.demo1.exceptions.Profile.UserNotFoundException;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@EnableAsync
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeGenerator verificationCodeGenerator;

    @Value("${custom.profile.default-url}")
    private String defaultProfileUrl;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService,VerificationCodeGenerator verificationCodeGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationCodeGenerator = verificationCodeGenerator;

    }

    public void signup(RegisterUserRequest input) {
        if (userRepository.existsByUsername(input.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(verificationCodeGenerator.generateCode());
        user.setVerificationCodeExpiresAt(verificationCodeGenerator.getExpirationTime());
        user.setEnabled(false);


        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());
        userProfile.setDescription("Welcome! This is my profile.");
        userProfile.setProfilePictureUrl(defaultProfileUrl);
        userProfile.setUser(user);
        user.setUserProfile(userProfile);


        sendVerificationEmail(user);
        userRepository.save(user);
    }


    public void authenticate(LoginUserRequest input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException("Account not verified. Please verify your account.");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // need to save this manually since we are not using built in config
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

    }

    public void verifyUser(VerifyUserRequest input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new ExpiredVerificationCodeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new InvalidVerificationCodeException("Invalid verification code");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.isEnabled()) {
            throw new AccountIsAlreadyVerifiedException("Account is already verified.");
        }

        user.setVerificationCode(verificationCodeGenerator.generateCode());
        user.setVerificationCodeExpiresAt(verificationCodeGenerator.getExpirationTime());

        userRepository.save(user);
        sendVerificationEmail(user);
    }


    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = buildEmailTemplate(verificationCode);

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}", user.getEmail(), e);
            throw new EmailSendingException("Unable to send verification email. Please try again.");
        }
    }

    private String buildEmailTemplate(String verificationCode) {
        return "<html>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0;\">"
                + "<div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h2 style=\"color: #333; text-align: center;\">Welcome to FilmSync!</h2>"
                + "<p style=\"font-size: 16px; color: #555; text-align: center;\">Thank you for joining our community. To continue, please enter the verification code below:</p>"
                + "<div style=\"background-color: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0; text-align: center;\">"
                + "<h3 style=\"color: #333; margin: 0;\">Verification Code:</h3>"
                + "<p style=\"font-size: 24px; font-weight: bold; color: #007bff; margin: 10px 0;\">" + verificationCode + "</p>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #777; text-align: center;\">If you did not request this email, please ignore it.</p>"
                + "<p style=\"font-size: 14px; color: #777; text-align: center;\">&copy; 2024 FilmSync. All rights reserved.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }



}
