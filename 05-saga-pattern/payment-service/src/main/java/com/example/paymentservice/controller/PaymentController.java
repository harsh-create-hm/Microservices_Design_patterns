package com.example.paymentservice.controller;

import com.example.paymentservice.model.Payment;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment processPayment(@RequestBody Map<String, Object> request) {
        return paymentService.processPayment(request);
    }

    @PostMapping("/refund/{orderId}")
    public Payment refundPayment(@PathVariable Long orderId) {
        return paymentService.refundPayment(orderId);
    }

    @GetMapping
    public Collection<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
}
