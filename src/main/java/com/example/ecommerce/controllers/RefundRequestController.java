package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.CreateRefundRequestDTO;
import com.example.ecommerce.models.RefundRequest;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.RefundRequestService;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/refund-requests")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RefundRequestController {

    private final RefundRequestService refundService;
    private final UserService userService;

    @PostMapping
    public RefundRequest create(@PathVariable Long orderId,
                                @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                @Valid @RequestBody CreateRefundRequestDTO body) {
        User user = userService.getUserByEmail(principal.getUsername());
        return refundService.create(orderId, user, body.reason());
    }
}
