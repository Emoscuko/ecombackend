package com.example.ecommerce.services;

import com.example.ecommerce.models.User;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;

    public User registerUser(String name, String email, String rawPassword) {
        if(userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);
        return userRepo.save(user);
    }

    public String login(String email, String password) {
        // Option 1: Use AuthenticationManager to authenticate
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        // If we reach here, authentication was successful (otherwise an exception is thrown)
        User user = userRepo.findByEmail(email).orElseThrow();
        // Generate JWT token
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Alternatively, without AuthenticationManager:
        // User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials"));
        // if (!passwordEncoder.matches(password, user.getPassword())) {
        //    throw new RuntimeException("Invalid credentials");
        // }
        // return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
