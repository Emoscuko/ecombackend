package com.example.ecommerce.controllers.seller;

import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

// src/main/java/com/example/ecommerce/controllers/seller/SellerDashboardController.java
@RestController
@RequestMapping("/api/seller/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class SellerDashboardController {

    private final ProductService productSvc;
    private final OrderService orderSvc;   // whatever service already fetches orders
    private final UserRepository userRepo;

    public record DashboardSummary(int products, int pendingOrders, BigDecimal totalSales) {}

    public SellerDashboardController(ProductService p, OrderService o, UserRepository u) {
        this.productSvc = p; this.orderSvc = o; this.userRepo = u;
    }

    @GetMapping
    public DashboardSummary summary(Principal principal) {
        Long sellerId = userRepo.findByEmail(principal.getName())
                .orElseThrow().getId();
        int prodCnt   = productSvc.getProductsBySeller(sellerId).size();
        int pendCnt   = orderSvc.countPendingForSeller(sellerId);   // write that query
        BigDecimal sales = orderSvc.sumPaidTotalForSeller(sellerId);
        return new DashboardSummary(prodCnt, pendCnt, sales);
    }
}
