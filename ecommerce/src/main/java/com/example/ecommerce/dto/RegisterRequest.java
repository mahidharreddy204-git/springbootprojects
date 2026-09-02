package com.example.ecommerce.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @Size(
        min=4,
        message = "password should be at least 4 characaters"
    )
    private String password;
    
    
    @Email(message = "invalid email fromat")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;
   

}
