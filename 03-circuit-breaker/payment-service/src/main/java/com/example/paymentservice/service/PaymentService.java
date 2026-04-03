package com.example.paymentservice.service;

import com.example.paymentservice.model.Payment;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class PaymentService {

    private final Map<Long, Payment> paymentStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);
    private final Random random = new Random();

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public Payment processPayment(Payment payment) {
        log.info("Processing payment for order: {}", payment.getOrderId());

        // Simulate intermittent external bank API failures
        if (random.nextInt(10) < 4) {
            throw new RuntimeException("External bank API is unavailable");
        }

        payment.setId(idGen.getAndIncrement());
        payment.setStatus("COMPLETED");
        payment.setMessage("Payment processed successfully");
        paymentStore.put(payment.getId(), payment);
        return payment;
    }

    public Payment paymentFallback(Payment payment, Exception ex) {
        log.warn("Circuit breaker activated for order {}: {}", payment.getOrderId(), ex.getMessage());
        payment.setId(idGen.getAndIncrement());
        payment.setStatus("FAILED");
        payment.setMessage("Payment service temporarily unavailable. Please try again later.");
        paymentStore.put(payment.getId(), payment);
        return payment;
    }

    public Payment getPayment(Long id) {
        Payment payment = paymentStore.get(id);
        if (payment == null) {
            throw new RuntimeException("Payment not found: " + id);
        }
        return payment;
    }
}
