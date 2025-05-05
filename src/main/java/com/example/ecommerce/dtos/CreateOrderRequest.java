package com.example.ecommerce.dtos;

import java.util.List;

public class CreateOrderRequest {
    public Long addressId;                 // ➕ NEW
    public List<OrderRequestItem> items;
}