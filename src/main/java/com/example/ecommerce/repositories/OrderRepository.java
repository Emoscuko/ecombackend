package com.example.ecommerce.repositories;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId);
    @Query("""
    SELECT DISTINCT o
    FROM Order o
    JOIN FETCH o.items i
    JOIN FETCH i.product p
    WHERE o.user.email = :email
    ORDER BY o.orderDate DESC
  """)
    List<Order> findByUserEmailWithItems(@Param("email") String email);
    boolean existsByUser_IdAndStatusAndItems_Product_Id(Long userId,
                                                        OrderStatus status,
                                                        Long productId);
}
