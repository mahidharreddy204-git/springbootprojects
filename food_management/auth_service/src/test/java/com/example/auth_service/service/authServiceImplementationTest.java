
package com.example.auth_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementationTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImplementation authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("Test User");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("password123");

        user = new User();
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");
    }

    // 1. Register user successfully
    @Test
    void registerUserTest() {

        when(userRepo.existsByEmail("test@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String response = authService.register(registerRequest);

        assertEquals(
                "User registered successfully",
                response
        );

        verify(userRepo).existsByEmail("test@gmail.com");

        verify(passwordEncoder).encode("password123");

        verify(userRepo).save(any(User.class));
    }

    // 2. Register with existing email
    @Test
    void registerExistingEmailTest() {

        when(userRepo.existsByEmail("test@gmail.com"))
                .thenReturn(true);

        String response = authService.register(registerRequest);

        assertEquals(
                "Email already exists",
                response
        );

        verify(userRepo, never()).save(any(User.class));

        verify(passwordEncoder, never()).encode(anyString());
    }

    // 3. Login successfully
    @Test
    void loginUserTest() {

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(
                new UsernamePasswordAuthenticationToken(
                        "test@gmail.com",
                        "password123"
                )
        );

        when(userRepo.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(
                "test@gmail.com",
                "USER"
        )).thenReturn("test-jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(userRepo).findByEmail("test@gmail.com");

        verify(jwtService).generateToken(
                "test@gmail.com",
                "USER"
        );
    }

    // 4. Login with invalid credentials
    @Test
    void loginInvalidCredentialsTest() {

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(
                new BadCredentialsException("Invalid credentials")
        );

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(userRepo, never()).findByEmail(anyString());

        verify(jwtService, never()).generateToken(
                anyString(),
                anyString()
        );
    }
}
