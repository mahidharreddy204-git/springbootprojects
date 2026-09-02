package com.example.ecommerce.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey1234";

    // GENERATE TOKEN
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // TOKEN VALIDATION
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // CHECK EXPIRATION
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // EXTRACT USERNAME
    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);
    }

    // EXTRACT EXPIRATION
    public Date extractExpiration(String token) {

        return extractClaims(token, Claims::getExpiration);
    }

    // GENERIC CLAIM EXTRACTION
    public <T> T extractClaims(
            String token,
            Function<Claims, T> resolver
    ) {

        final Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // EXTRACT ALL CLAIMS
    public Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // SECRET KEY
    public Key getSignInKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }
}