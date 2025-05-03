package com.example.ecommerce.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@RequiredArgsConstructor
public class StripeConfig {

    @Value("${stripe.secretKey}") private String secretKey;
    @PostConstruct
    void init() { Stripe.apiKey = secretKey; }
}

