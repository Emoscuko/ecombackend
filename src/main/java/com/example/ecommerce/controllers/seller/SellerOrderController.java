package com.example.ecommerce.controllers.seller;

import com.example.ecommerce.dtos.SellerOrderItemDto;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping
    public List<SellerOrderItemDto> listMyOrderItems(Authentication auth) {
        String sellerEmail = auth.getName();
        List<OrderItem> items = orderItemRepository.findBySellerEmail(sellerEmail);

        return items.stream()
                .map(oi -> new SellerOrderItemDto(
                        oi.getId(),
                        oi.getOrder().getOrderDate(),
                        oi.getQuantity(),
                        oi.getPrice(),
                        oi.getProduct().getName(),
                        oi.getOrder().getStatus(),
                        oi.getOrder().getUser().getEmail()
                ))
                .collect(Collectors.toList());
    }
}
