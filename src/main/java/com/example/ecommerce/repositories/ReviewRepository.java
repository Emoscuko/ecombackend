package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_Id(Long productId);
    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);

}
