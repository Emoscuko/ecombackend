package com.example.ecommerce.dtos;

public record ProductRequest(
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        Long sellerId,
        Long   categoryId
) {}
