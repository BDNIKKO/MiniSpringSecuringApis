package org.example.minispringsecuringapis.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final ConcurrentHashMap<String, UserDetails> users = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for PasswordEncoder
    public JwtUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    // Method to save a user with an encoded password
    public void saveUser(String username, UserDetails userDetails, boolean isPasswordRaw) {
        // Encode the password only if it's raw (i.e., not already hashed)
        String passwordToSave = isPasswordRaw
                ? passwordEncoder.encode(userDetails.getPassword())
                : userDetails.getPassword();

        UserDetails encodedUser = org.springframework.security.core.userdetails.User.withUsername(userDetails.getUsername())
                .password(passwordToSave)
                .authorities(userDetails.getAuthorities())
                .build();

        users.put(username, encodedUser);
    }
}
