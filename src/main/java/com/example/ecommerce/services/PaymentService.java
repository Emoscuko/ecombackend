package com.example.ecommerce.services;

import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.Payment;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;

    @Transactional
    public void refundPayment(Long paymentId) throws StripeException {
        // 1️⃣ load our Payment record
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        // 2️⃣ retrieve the PaymentIntent from Stripe
        PaymentIntent pi = PaymentIntent.retrieve(payment.getPaymentIntentId());

        // 3️⃣ get the ID of the latest Charge on that intent
        String chargeId = pi.getLatestCharge(); // ↪︎ uses getLatestCharge() :contentReference[oaicite:0]{index=0}

        // 4️⃣ call the Refund API
        RefundCreateParams params = RefundCreateParams.builder()
                .setCharge(chargeId)
                .build();
        Refund.create(params); // ↪︎ calls Refund.create(...) :contentReference[oaicite:1]{index=1}

        // 5️⃣ update our local record
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepo.save(payment);
    }

    /** Create Stripe intent & persist a PENDING Payment */
    @Transactional
    public String createPayment(Long orderId, Long amount, String currency) throws StripeException {
        // 1. create Stripe intent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true).build())
                .build();

        PaymentIntent pi = PaymentIntent.create(params);

        // 2. persist our Payment entity
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(order.getUser());
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

        return pi.getClientSecret();
    }

    /** Admin: fetch all payments */
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    /** Mark payment status after a webhook (if you wire one up) */
    @Transactional
    public void updateStatus(Long paymentId, PaymentStatus status) {
        Payment p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        p.setStatus(status);
        paymentRepo.save(p);
    }
}
