package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.dtos.RefundDecisionDto;
import com.example.ecommerce.enums.RefundStatus;
import com.example.ecommerce.models.RefundRequest;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.RefundRequestService;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/refund-requests")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdminRefundController {

    private final RefundRequestService refundService;
    private final UserService userService;

    @GetMapping
    public List<RefundRequest> list(@RequestParam(required = false) RefundStatus status) {
        return refundService.list(status);
    }

    @PatchMapping("/{id}")
    public RefundRequest decide(@PathVariable Long id,
                                @Valid @RequestBody RefundDecisionDto body,
                                @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User admin = userService.getUserByEmail(principal.getUsername());
        return refundService.decide(id, body.accept(), body.comment(), admin);
    }
}
