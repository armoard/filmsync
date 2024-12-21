package com.example.demo1.controller.navbar;

import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.NoAuthenticatedUserFoundException;
import com.example.demo1.service.security.SecurityService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NavbarControllerAdvice {

    private final SecurityService securityService;

    public NavbarControllerAdvice(SecurityService securityService) {
        this.securityService = securityService;
    }

    // add the basic user info to all models to use it in the navbar
    @ModelAttribute("navbarUserProfile")
    public UserProfile addAuthenticatedUserProfile() {
        try {
            return securityService.getAuthenticatedUserProfile();
        } catch (NoAuthenticatedUserFoundException e) {
            return null; // need this for /welcome
        }
    }

}