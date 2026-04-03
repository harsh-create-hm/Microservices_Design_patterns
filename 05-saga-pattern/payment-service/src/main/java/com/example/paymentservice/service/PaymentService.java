package com.example.paymentservice.service;

import com.example.paymentservice.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class PaymentService {

    private final Map<Long, Payment> paymentStore = new ConcurrentHashMap<>();
    private final Map<Long, Payment> paymentsByOrderId = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Payment processPayment(Map<String, Object> request) {
        Long orderId = Long.valueOf(request.get("orderId").toString());
        String customerId = request.get("customerId").toString();
        Double amount = Double.valueOf(request.get("amount").toString());

        Payment payment = new Payment(idGen.getAndIncrement(), orderId, customerId, amount, "PROCESSED");
        paymentStore.put(payment.getId(), payment);
        paymentsByOrderId.put(orderId, payment);
        log.info("Saga: Payment {} processed for order {}", payment.getId(), orderId);
        return payment;
    }

    public Payment refundPayment(Long orderId) {
        Payment payment = paymentsByOrderId.get(orderId);
        if (payment != null) {
            payment.setStatus("REFUNDED");
            log.info("Saga compensation: Payment refunded for order {}", orderId);
        }
        return payment;
    }

    public Collection<Payment> getAllPayments() {
        return paymentStore.values();
    }
}
