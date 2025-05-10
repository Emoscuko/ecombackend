package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.CreateOrderRequest;
import com.example.ecommerce.dtos.OrderRequestItem;
import com.example.ecommerce.models.*;
import com.example.ecommerce.services.AddressService;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                    oi.setProduct(new Product(itemReq.productId,true , null, null,
                            null, null, null, null, null));
                    oi.setQuantity(itemReq.quantity);
                    return oi;
                })
                .toList();


        return orderService.placeOrder(user, shipping, orderItems);
    }
    @GetMapping("/{id}")
    public Order getOne(@PathVariable Long id,
                        @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Order order = orderService.getOrderById(id);

        // Optional: allow only the owner OR an admin
        if (!order.getUser().getEmail().equals(principal.getUsername()) &&
                !principal.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your order");
        }
        return order;
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
