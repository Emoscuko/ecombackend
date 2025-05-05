// src/main/java/com/example/ecommerce/dtos/UserProfileDTO.java
package com.example.ecommerce.dtos;

import java.util.List;

public record UserProfileDTO(Long id, String name,
                             String email, List<AddressDTO> addresses) { }
