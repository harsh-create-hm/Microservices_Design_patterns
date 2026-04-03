package com.example.orderservice.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class OrderService {

    private final Map<Long, Map<String, Object>> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Map<String, Object> processOrder(Map<String, Object> request) {
        Long orderId = idGen.getAndIncrement();

        // These calls are protected by separate bulkheads
        String paymentResult = callPaymentService(orderId, request);
        String inventoryResult = callInventoryService(orderId, request);

        Map<String, Object> order = new ConcurrentHashMap<>();
        order.put("orderId", orderId);
        order.put("customerId", request.get("customerId"));
        order.put("productId", request.get("productId"));
        order.put("quantity", request.get("quantity"));
        order.put("paymentStatus", paymentResult);
        order.put("inventoryStatus", inventoryResult);
        order.put("status", "CREATED");
        orderStore.put(orderId, order);
        return order;
    }

    @Bulkhead(name = "paymentService", fallbackMethod = "paymentFallback")
    public String callPaymentService(Long orderId, Map<String, Object> request) {
        log.info("Bulkhead: Calling payment service for order {}", orderId);
        // Simulate payment call with potential delay
        simulateServiceCall();
        return "PAYMENT_PROCESSED";
    }

    public String paymentFallback(Long orderId, Map<String, Object> request, Throwable t) {
        log.warn("Bulkhead full for payment service, order {}: {}", orderId, t.getMessage());
        return "PAYMENT_QUEUED";
    }

    @Bulkhead(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public String callInventoryService(Long orderId, Map<String, Object> request) {
        log.info("Bulkhead: Calling inventory service for order {}", orderId);
        simulateServiceCall();
        return "INVENTORY_RESERVED";
    }

    public String inventoryFallback(Long orderId, Map<String, Object> request, Throwable t) {
        log.warn("Bulkhead full for inventory service, order {}: {}", orderId, t.getMessage());
        return "INVENTORY_CHECK_PENDING";
    }

    private void simulateServiceCall() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Map<Long, Map<String, Object>> getAllOrders() {
        return orderStore;
    }
}
