package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.models.Payment;
import com.example.ecommerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdminPaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAllPayments();
    }
}
