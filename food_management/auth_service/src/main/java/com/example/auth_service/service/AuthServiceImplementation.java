package com.example.auth_service.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthServiceImplementation  implements  AuthService{

    private final  UserRepository userRepo;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    
    private  final  PasswordEncoder passwordEncoder;

    @Override
public String register(RegisterRequest request) {

    if (userRepo.existsByEmail(request.getEmail())) {
        return "Email already exists";
    }

    User user = new User();

    user.setName(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole("USER");

    userRepo.save(user);

    return "User registered successfully";
}

    @Override 
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    User user = userRepo.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

        return new AuthResponse(token);
    }
    
}
