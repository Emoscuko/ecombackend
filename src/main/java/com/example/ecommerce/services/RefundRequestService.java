package com.example.ecommerce.services;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.RefundStatus;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.RefundRequest;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.RefundRequestRepository;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundRequestService {

    private final RefundRequestRepository refundRepo;
    private final OrderRepository orderRepo;
    private final PaymentService paymentService;   // already exists

    /** Customer: create a new refund request */
    @Transactional
    public RefundRequest create(Long orderId, User user, String reason) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // sanity checks
        if (!order.getUser().getId().equals(user.getId()))
            throw new IllegalStateException("Order doesn't belong to user");

        if (order.getStatus() != OrderStatus.DELIVERED)
            throw new IllegalStateException("Only delivered orders can be refunded");

        if (refundRepo.existsByOrder_IdAndStatus(orderId, RefundStatus.PENDING))
            throw new IllegalStateException("There’s already a pending refund request");

        RefundRequest rr = new RefundRequest();
        rr.setOrder(order);
        rr.setUser(user);
        rr.setReason(reason);
        return refundRepo.save(rr);
    }

    /** Admin: list by status */
    public List<RefundRequest> list(RefundStatus status) {
        return (status == null) ? refundRepo.findAll() : refundRepo.findByStatus(status);
    }

    /** Admin: accept / reject */
    @Transactional
    public RefundRequest decide(Long id, boolean accept, String comment, User admin) {
        RefundRequest rr = refundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund request not found"));

        if (rr.getStatus() != RefundStatus.PENDING)
            throw new IllegalStateException("Already decided");

        rr.setStatus(accept ? RefundStatus.ACCEPTED : RefundStatus.REJECTED);
        rr.setAdminComment(comment);
        rr.setDecidedAt(LocalDateTime.now());
        rr.setDecidedBy(admin);
        refundRepo.save(rr);

        if (accept) {
            // trigger payment refund & update order
            try {
                paymentService.refundPayment(rr.getOrder().getPayment().getId());
                rr.getOrder().setStatus(OrderStatus.REFUNDED);
            } catch (StripeException e) {
                throw new RuntimeException("Failed to refund payment", e);
            }
        }

        return rr;
    }
}
