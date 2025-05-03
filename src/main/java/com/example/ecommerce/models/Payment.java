package com.example.ecommerce.models;

import com.example.ecommerce.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_intent_id", unique = true)
    private String paymentIntentId;


    private Long userId;
    private Long amount;
    private String currency;
    private PaymentStatus status;
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonBackReference
    @OneToOne @JoinColumn(name = "order_id")
    private Order order;
}

