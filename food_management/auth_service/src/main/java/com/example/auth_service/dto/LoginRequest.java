package com.example.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class LoginRequest {

    @NotBlank (message = "email is required")
    private String email;

    @NotBlank (message="password is rquired")
    @Size(min=5,max=10,message="password must be atleast 10 characters")
    private String password;


}
