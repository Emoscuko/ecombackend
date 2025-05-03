// src/main/java/com/example/ecommerce/repositories/PaymentRepository.java
package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /** fetch the Payment record that wraps this order */
    Optional<Payment> findByOrder_Id(Long orderId);
}
