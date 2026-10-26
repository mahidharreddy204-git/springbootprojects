package com.example.food_service.security;

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

                // Food items
                .requestMatchers(HttpMethod.GET, "/item/**")
                    .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/item/**")
                    .hasRole("ADMIN")
                 .requestMatchers(HttpMethod.PUT,"/item/reduce-stock/**")
                        .permitAll()
                .requestMatchers(HttpMethod.PUT, "/item/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/item/**")
                    .hasRole("ADMIN")
        
                // Categories
                .requestMatchers(HttpMethod.GET, "/category/**")
                    .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/category/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/category/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/category/**")
                    .hasRole("ADMIN")

                // Subcategories
                .requestMatchers(HttpMethod.GET, "/subcategory/**")
                    .hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/subcategory/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/subcategory/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/subcategory/**")
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
