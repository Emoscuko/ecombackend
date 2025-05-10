// src/main/java/com/example/ecommerce/dtos/ProductRequest.java
package com.example.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull    Double price,
        @NotNull    Integer stock,
        @NotBlank   String imageUrl,
        @NotNull    Long   sellerId,
        @NotNull    Long   categoryId
) {}
