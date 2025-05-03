package com.example.ecommerce.controllers;

import com.example.ecommerce.services.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;



    /** 1️⃣ Frontend calls this with cart total (in cents) */
    @PostMapping("/create-payment-intent")
    public Map<String, String> create(
            @RequestBody Map<String, Object> payload) throws StripeException {
        Long amount   = Long.valueOf(payload.get("amount").toString());
        String currency = payload.getOrDefault("currency","eur").toString();
        Long orderId  = Long.valueOf(payload.get("orderId").toString());

        String clientSecret = service.createPayment(orderId, amount, currency);
        return Map.of("clientSecret", clientSecret);
    }


//    /** 2️⃣ (optional) webhook keeps Order & Payment in sync */
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handle(@RequestBody String body,
//                                         @RequestHeader("Stripe-Signature") String sig)
//            throws StripeException, SignatureVerificationException {
//
//        Event event = Webhook.constructEvent(body, sig, webhookSecret);
//        if ("payment_intent.succeeded".equals(event.getType())) {
//            PaymentIntent pi = (PaymentIntent)event.getDataObjectDeserializer()
//                    .getObject().orElse(null);
//            // look up Payment by paymentIntentId, mark status=SUCCEEDED,
//            // create Order if you deferred it until now, etc.
//        }
//        return ResponseEntity.ok("");
//    }
}

