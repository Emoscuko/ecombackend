package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.CreateOrderRequest;
import com.example.ecommerce.dtos.OrderRequestItem;
import com.example.ecommerce.models.*;
import com.example.ecommerce.services.AddressService;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
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
    @Autowired private AddressService addressService;

    @PostMapping
    public Order placeOrder(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
                            @RequestBody CreateOrderRequest request) {

        User user = userService.getUserByEmail(userDetails.getUsername());

        Address shipping = addressService     // inject it first
                .getByIdForUser(user, request.addressId);

        List<OrderItem> orderItems = request.items.stream()
                .map(itemReq -> {
                    OrderItem oi = new OrderItem();
                    // only the ID is needed here; OrderService will fetch the full Product
                    oi.setProduct(new Product(itemReq.productId, null, null,
                            null, null, null, null, null));
                    oi.setQuantity(itemReq.quantity);
                    return oi;
                })
                .toList();


        return orderService.placeOrder(user, shipping, orderItems);
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
