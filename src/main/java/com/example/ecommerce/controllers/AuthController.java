package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.AuthResponse;
import com.example.ecommerce.dtos.LoginRequest;
import com.example.ecommerce.dtos.RegisterRequest;
import com.example.ecommerce.services.AuthService;
import com.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired private AuthService authService;



    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        // Register new user as customer
        return authService.registerUser(request.name, request.email, request.password);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request.email, request.password);
        return new AuthResponse(token);
    }
}
