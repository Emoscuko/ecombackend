// src/main/java/com/example/ecommerce/repositories/SellerApplicationRepository.java
package com.example.ecommerce.repositories;

import com.example.ecommerce.enums.SellerApplicationStatus;
import com.example.ecommerce.models.SellerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SellerApplicationRepository
        extends JpaRepository<SellerApplication, Long> {
    List<SellerApplication> findByStatus(SellerApplicationStatus status);
    boolean existsByUserIdAndStatus(Long userId, SellerApplicationStatus status);
}
