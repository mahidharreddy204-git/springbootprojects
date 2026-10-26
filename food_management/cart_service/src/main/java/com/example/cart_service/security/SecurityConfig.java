package com.example.cart_service.security;

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

                // Cart
                .requestMatchers(HttpMethod.GET, "/cart/**")
                    .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/cart/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/cart/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/cart/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/cart/checkout")
                    .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/cart/clear")
                     .hasAnyRole("USER", "ADMIN")

              

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
