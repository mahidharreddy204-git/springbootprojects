package com.example.order_service.security;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {

    private final String  SECRET_KEY = 
            "mysecretkeymysecretkeymysecretkey12345";
        
   private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

   public String extractEmail(String token){
    return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
   }
   public String extractRole(String token) {
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role", String.class);
}
   
   public boolean isTokenValid(String token){
    try{
        extractEmail(token);
        return true;
    }
    catch(Exception e){
        return false;
    }
   }
}
