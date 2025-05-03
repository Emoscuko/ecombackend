// src/main/java/com/example/ecommerce/models/SellerApplication.java
package com.example.ecommerce.models;

import com.example.ecommerce.enums.SellerApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "seller_applications")
public class SellerApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)           // who applied
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SellerApplicationStatus status = SellerApplicationStatus.PENDING;

    private String note;                   // optional extra message from the user
}
