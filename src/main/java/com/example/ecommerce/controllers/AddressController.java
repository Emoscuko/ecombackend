// src/main/java/com/example/ecommerce/controllers/AddressController.java
package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.AddressDTO;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.AddressService;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    @Autowired private AddressService addressService;
    @Autowired private UserService userService;

    private User currentUser(String email) { return userService.getUserByEmail(email); }

    @GetMapping
    public List<AddressDTO> list(@AuthenticationPrincipal org.springframework.security.core.userdetails.User p) {
        return addressService.getMyAddresses(currentUser(p.getUsername()));
    }

    @PostMapping
    public AddressDTO add(@AuthenticationPrincipal org.springframework.security.core.userdetails.User p,
                          @RequestBody AddressDTO dto) {
        return addressService.addAddress(currentUser(p.getUsername()), dto);
    }

    @PutMapping("{id}")
    public AddressDTO update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User p,
                             @PathVariable Long id,
                             @RequestBody AddressDTO dto) {
        return addressService.updateAddress(currentUser(p.getUsername()), id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@AuthenticationPrincipal org.springframework.security.core.userdetails.User p,
                       @PathVariable Long id) {
        addressService.deleteAddress(currentUser(p.getUsername()), id);
    }
}
