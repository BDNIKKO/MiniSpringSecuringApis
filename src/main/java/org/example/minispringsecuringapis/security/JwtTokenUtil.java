package org.example.minispringsecuringapis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    // Inject the secret key and expiration time via application.properties or application.yml
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationTime;

    // Use constructor-based injection to ensure the values are injected properly
    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey,
                        @Value("${jwt.expiration}") long jwtExpirationTime) {
        this.secretKey = secretKey;
        this.jwtExpirationTime = jwtExpirationTime;
    }

    // Decode the Base64-encoded secret key and return a Key object
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract specific claims from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Use the decoded secret key to verify the token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate a new token for a given username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Create the JWT token with claims, subject, issuedAt, expiration, and signing key
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))  // Use expiration time from properties
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Sign with the secure key using HS256 algorithm
                .compact();
    }

    // Validate the token by checking if the username matches and if the token has expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired: " + e.getMessage());
            return false;  // Return false if the token is expired
        }
    }
}
