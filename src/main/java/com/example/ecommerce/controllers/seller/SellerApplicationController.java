package com.example.ecommerce.controllers.seller;

import com.example.ecommerce.models.SellerApplication;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.SellerApplicationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/seller-applications")
@CrossOrigin(origins = "http://localhost:4200")
public class SellerApplicationController {
    private final SellerApplicationService service;
    private final UserRepository userRepo;
    public record ApplyDto(String note) {}

    public SellerApplicationController(SellerApplicationService s, UserRepository u) {
        this.service = s; this.userRepo = u;
    }

    @PostMapping
    public SellerApplication apply(@RequestBody ApplyDto dto,
                                   Principal principal) {
        User u = userRepo.findByEmail(principal.getName())
                .orElseThrow();
        return service.apply(u, dto.note());
    }
}
