package com.example.order_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(auth -> auth

                    // Get orders
                    .requestMatchers(HttpMethod.GET, "/orders/**")
                        .hasAnyRole("USER", "ADMIN")

                    // Place an order
                    .requestMatchers(HttpMethod.POST, "/orders/**")
                        .hasAnyRole("USER", "ADMIN")

                    // Update an order
                    .requestMatchers(HttpMethod.PUT, "/orders/**")
                        .hasRole("ADMIN")

                    // Any other endpoint requires authentication
                    .anyRequest().authenticated()
                )
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
