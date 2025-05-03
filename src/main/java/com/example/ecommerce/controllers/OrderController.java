package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;
    //DTO classes for request/response can be used for clarity
    static class OrderRequestItem { public Long productId; public Integer quantity; }
    static class CreateOrderRequest { public List<OrderRequestItem> items; }

    @PostMapping
    public Order placeOrder(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
                            @RequestBody CreateOrderRequest request) {
        // Find the actual User entity by email (username)
        User user = userService.getUserById(
                userService.getAllUsers().stream()
                        .filter(u -> u.getEmail().equals(userDetails.getUsername()))
                        .findFirst().orElseThrow().getId()
        );
        // Convert request items to OrderItem entities
        List<OrderItem> orderItems = request.items.stream().map(itemReq -> {
            OrderItem oi = new OrderItem();
            oi.setProduct(new com.example.ecommerce.models.Product(itemReq.productId, null, null, null, null, null, null,null));
            oi.setQuantity(itemReq.quantity);
            return oi;
        }).toList();
        return orderService.placeOrder(user, orderItems);
    }

    @GetMapping
    public List<Order> listMyOrders(Principal principal) {
        String email = principal.getName();
        return orderService.getOrdersByUserEmail(email);
    }
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> updatePaymentIntent(
            @PathVariable Long orderId,
            @RequestBody Map<String,String> body) {
        String piId = body.get("paymentIntentId");
        orderService.linkPaymentIntent(orderId, piId);
        return ResponseEntity.noContent().build();
    }
}
