// src/main/java/com/example/ecommerce/repositories/OrderItemRepository.java
package com.example.ecommerce.repositories;

import com.example.ecommerce.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
      SELECT oi
      FROM OrderItem oi
      JOIN FETCH oi.order o
      JOIN FETCH o.user u
      JOIN FETCH oi.product p
      WHERE p.seller.email = :sellerEmail
    """)
    List<OrderItem> findBySellerEmail(@Param("sellerEmail") String sellerEmail);
}
