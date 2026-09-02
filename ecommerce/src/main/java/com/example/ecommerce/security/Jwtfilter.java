package com.example.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.
        SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.
        WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

@RequiredArgsConstructor
public class Jwtfilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain
    ) throws ServletException, IOException {

        // Get Authorization Header
        final String authHeader =
                request.getHeader("Authorization");

        // If Header Missing or Invalid
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        // Extract JWT Token
        String jwt = authHeader.substring(7);

        // Extract Username from Token
        String username =
                jwtService.extractUsername(jwt);

        // Check Authentication
        if (username != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            // Load User from Database
            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            // Validate Token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Create Authentication Token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Add Request Details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Set Authentication
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // Continue Filter Chain
        filterChain.doFilter(request, response);
    }
}