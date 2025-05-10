package com.example.ecommerce.repositories;

import com.example.ecommerce.enums.RefundStatus;
import com.example.ecommerce.models.RefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {
    List<RefundRequest> findByStatus(RefundStatus status);
    boolean existsByOrder_IdAndStatus(Long orderId, RefundStatus status);
}
