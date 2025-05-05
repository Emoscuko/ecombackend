// src/main/java/com/example/ecommerce/dtos/AddressDTO.java
package com.example.ecommerce.dtos;

public record AddressDTO(Long id, String street,
                         String city, String state, String zip) { }
