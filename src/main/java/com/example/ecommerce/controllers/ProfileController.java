// src/main/java/com/example/ecommerce/controllers/ProfileController.java
package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.UserProfileDTO;
import com.example.ecommerce.services.AddressService;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    @Autowired private UserService userService;
    @Autowired private AddressService addressService;

    @GetMapping
    public UserProfileDTO me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User user = userService.getUserByEmail(principal.getUsername());
        return new UserProfileDTO(
                user.getId(), user.getName(), user.getEmail(),
                addressService.getMyAddresses(user)
        );
    }
}
