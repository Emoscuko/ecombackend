// src/main/java/com/example/ecommerce/dtos/SellerOrderItemDto.java
package com.example.ecommerce.dtos;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SellerOrderItemDto {
    private Long id;
    private LocalDateTime orderDate;
    private Integer quantity;
    private Double price;
    private String productName;
    private OrderStatus orderStatus;
    private String buyerEmail;
}
