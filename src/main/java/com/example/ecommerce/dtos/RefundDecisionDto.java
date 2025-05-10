// RefundDecisionDto.java
package com.example.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefundDecisionDto(
        @NotNull Boolean accept,
        @Size(max = 800) String comment) {}