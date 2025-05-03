package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.models.SellerApplication;
import com.example.ecommerce.services.SellerApplicationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/seller-applications")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminSellerApplicationController {
    private final SellerApplicationService service;
    public AdminSellerApplicationController(SellerApplicationService s) { this.service = s; }

    @GetMapping                             // list pending
    public List<SellerApplication> list() { return service.listPending(); }

    @PutMapping("/{id}/approve")
    public SellerApplication approve(@PathVariable Long id) { return service.approve(id); }

    @PutMapping("/{id}/reject")
    public SellerApplication reject(@PathVariable Long id) { return service.reject(id); }
}
