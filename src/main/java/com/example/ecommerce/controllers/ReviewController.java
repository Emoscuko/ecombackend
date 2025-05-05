package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.ReviewRequest;
import com.example.ecommerce.services.ReviewService;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.models.Review;
import com.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired private UserService   userService;

    // ---------- DTOs ----------

    record ReviewResponse(Long id, int rating, String comment,
                          String author, LocalDateTime createdAt) {
        static ReviewResponse of(Review r) {
            return new ReviewResponse(r.getId(), r.getRating(), r.getComment(),
                    r.getUser().getName(), r.getCreatedAt());
        }
    }

    // ---------- Create ----------
    @PostMapping
    public ReviewResponse add(@PathVariable Long productId,
                              @AuthenticationPrincipal
                              org.springframework.security.core.userdetails.User u,
                              @RequestBody ReviewRequest req) {

        User user = userService.getAllUsers().stream()
                .filter(x -> x.getEmail().equals(u.getUsername()))
                .findFirst().orElseThrow();

        return ReviewResponse.of(
                reviewService.addReview(user, productId, req.rating, req.comment)
        );
    }

    // ---------- List ----------
    @GetMapping
    public List<ReviewResponse> list(@PathVariable Long productId) {
        return reviewService.getReviews(productId)
                .stream().map(ReviewResponse::of).toList();
    }

    // ---------- Delete (ADMIN only) ----------
    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}


