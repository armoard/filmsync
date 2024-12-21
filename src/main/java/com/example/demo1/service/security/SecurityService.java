package com.example.demo1.service.security;

import com.example.demo1.entity.User;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.NoAuthenticatedUserFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {


    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (User) principal;
        }
        return null;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
        throw new IllegalStateException("No authenticated user found");
    }
    public UserProfile getAuthenticatedUserProfile() {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser != null) {
            UserProfile userProfile = authenticatedUser.getUserProfile();
            if (userProfile != null) {
                return userProfile;
            } else {
                throw new IllegalStateException("No profile associated with the authenticated user");
            }
        }
        throw new NoAuthenticatedUserFoundException("No authenticated user found");
    }

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public boolean isOwnProfile(String username){
        User authenticatedUser = getAuthenticatedUser();
        return authenticatedUser != null && authenticatedUser.getUsername().equals(username);
    }
    public boolean isUserLoggedIn(){
        return getAuthenticatedUser() != null;
    }

}