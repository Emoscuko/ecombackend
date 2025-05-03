package com.example.ecommerce.services;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.models.Review;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ReviewRepository;
import com.example.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private OrderRepository orderRepo;

    public Review addReview(User user, Long productId, int rating, String comment) {
        /* one‑review‑per‑user‑per‑product guard */
        if (reviewRepo.existsByUser_IdAndProduct_Id(user.getId(), productId))
            throw new IllegalStateException("You already reviewed this product");

        /* has the user actually received it? */
        boolean delivered = orderRepo
                .existsByUser_IdAndStatusAndItems_Product_Id(
                        user.getId(), OrderStatus.DELIVERED, productId);

        if (!delivered)
            throw new IllegalStateException("You can review only after delivery");

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review(null, rating, comment, LocalDateTime.now(),
                product, user);
        return reviewRepo.save(review);
    }

    public List<Review> getReviews(Long productId) {
        return reviewRepo.findByProduct_Id(productId);
    }

    public void deleteReview(Long id) { reviewRepo.deleteById(id); }
}

