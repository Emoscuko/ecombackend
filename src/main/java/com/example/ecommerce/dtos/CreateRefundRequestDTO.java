// CreateRefundRequestDto.java
package com.example.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRefundRequestDTO(
        @NotBlank @Size(max = 800) String reason) {}