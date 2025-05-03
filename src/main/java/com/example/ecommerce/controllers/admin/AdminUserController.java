package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.dtos.EnableUserRequest;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminUserController {
    @Autowired private UserService userService;

    @GetMapping
    public List<User> listUsers() {
        return userService.getAllUsers();
    }
    // Additional endpoints like update role or delete user can be added similarly.
    @PutMapping("/{id}/enable")
    public ResponseEntity<User> setUserEnabled(
            @PathVariable Long id,
            @RequestBody EnableUserRequest req
    ) {
        User updated = userService.setUserEnabled(id, req.isEnabled());
        return ResponseEntity.ok(updated);
    }
}

