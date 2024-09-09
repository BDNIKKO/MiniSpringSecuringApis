package org.example.minispringsecuringapis.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Generates a secure 256-bit key for HS256
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Converts the key to Base64 encoded string for storage
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        // Prints the key
        System.out.println("Your secure key: " + base64Key);
    }
}
