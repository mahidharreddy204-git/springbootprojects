package com.example.ecommerce.service;

import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;

@Service
public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
