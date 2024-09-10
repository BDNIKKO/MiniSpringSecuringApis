package org.example.minispringsecuringapis.security;

import org.example.minispringsecuringapis.model.User;
import org.example.minispringsecuringapis.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .build();
    }

    public void saveUser(String username, UserDetails userDetails, boolean isPasswordRaw) {
        String passwordToSave = isPasswordRaw
                ? passwordEncoder.encode(userDetails.getPassword())
                : userDetails.getPassword();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordToSave);
        newUser.setRoles(userDetails.getAuthorities().toString());

        userRepository.save(newUser);
    }
}
