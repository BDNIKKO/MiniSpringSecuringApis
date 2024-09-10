package org.example.minispringsecuringapis.controller;

import org.example.minispringsecuringapis.model.AuthenticationRequest;
import org.example.minispringsecuringapis.model.AuthenticationResponse;
import org.example.minispringsecuringapis.model.ApiResponse;
import org.example.minispringsecuringapis.security.JwtTokenUtil;
import org.example.minispringsecuringapis.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> createAuthenticationToken(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Incorrect username or password", null));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new ApiResponse<>("Login successful", new AuthenticationResponse(jwt)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        // Check if user already exists
        UserDetails existingUser;
        try {
            existingUser = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            return ResponseEntity.badRequest().body(new ApiResponse<>("User already exists", null));
        } catch (Exception e) {
            // If user is not found, proceed with registration
            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(authenticationRequest.getUsername())
                    .password(passwordEncoder.encode(authenticationRequest.getPassword()))  // Use PasswordEncoder to encode the password
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))  // Assign default role "ROLE_USER"
                    .build();

            userDetailsService.saveUser(authenticationRequest.getUsername(), userDetails, true); // updated to include boolean parameter
            return ResponseEntity.ok(new ApiResponse<>("User registered successfully", "Success"));
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> getProtectedData() {
        return ResponseEntity.ok("This is a protected endpoint. Only authenticated users can see this.");
    }
}
