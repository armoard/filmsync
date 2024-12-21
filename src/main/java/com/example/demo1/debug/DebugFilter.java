package com.example.demo1.debug;

/*
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("----- Debug Filter -----");
        System.out.println("Processing request: " + request.getRequestURI());
        System.out.println("Session ID: " + (request.getSession(false) != null ? request.getSession(false).getId() : "No session"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("User is authenticated: " + auth.getName());
        } else {
            System.out.println("No authenticated user found.");
        }


        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies present in the request.");
        }

        System.out.println("-------------------------");


        filterChain.doFilter(request, response);
    }
}

*/