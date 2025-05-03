package com.example.ecommerce.config;

import com.example.ecommerce.security.JwtAuthFilter;
import com.example.ecommerce.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired private JwtAuthFilter jwtAuthFilter;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());  // Disable CSRF for APIs
        http.cors(cors -> {});  // Enable CORS with default settings (see CorsConfig below)

        // Set session management to stateless (because we're using JWTs)
        http.sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

        // Configure URL-based authorization
        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        /* allow sellers */
                        .requestMatchers("/api/seller/**").hasRole("SELLER")

                        /* customer can file an application */
                        .requestMatchers(HttpMethod.POST,   "/api/products/*/reviews/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/seller-applications/**").hasRole("CUSTOMER")

                        /* admin areas */
                        .requestMatchers("/api/admin/seller-applications/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/*/reviews/**").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyRole("CUSTOMER", "SELLER", "ADMIN")


                        .anyRequest().authenticated()
                );

        // Add JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose AuthenticationManager bean to use in AuthService (for login authentication if needed)
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
