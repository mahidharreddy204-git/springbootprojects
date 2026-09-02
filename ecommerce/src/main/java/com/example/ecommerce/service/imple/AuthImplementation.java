package com.example.ecommerce.service.imple;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtService;
import com.example.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor

public class AuthImplementation implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepo;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest request){

        User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
             user.setRole(
            "ROLE_" + request.getRole().toUpperCase()
    );
            userRepo.save(user);
        
    }

    @Override
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword())
        );

        String token = jwtService.generateToken(request.getUsername());

        return new AuthResponse(token);        
    }
    



}
