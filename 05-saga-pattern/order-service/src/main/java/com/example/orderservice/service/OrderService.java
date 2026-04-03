package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate;
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Order createOrder(Order order) {
        order.setId(idGen.getAndIncrement());
        order.setStatus("PENDING");
        orderStore.put(order.getId(), order);
        log.info("Saga started: Order {} created", order.getId());

        try {
            // Step 1: Process Payment
            processPayment(order);
            log.info("Saga step 1 complete: Payment processed for order {}", order.getId());

            // Step 2: Reserve Inventory
            reserveInventory(order);
            log.info("Saga step 2 complete: Inventory reserved for order {}", order.getId());

            order.setStatus("CONFIRMED");
            log.info("Saga completed successfully: Order {} confirmed", order.getId());
        } catch (Exception e) {
            log.error("Saga failed for order {}: {}. Executing compensations.", order.getId(), e.getMessage());
            compensate(order);
        }

        return order;
    }

    private void processPayment(Order order) {
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderId", order.getId());
        paymentRequest.put("customerId", order.getCustomerId());
        paymentRequest.put("amount", order.getAmount());
        restTemplate.postForObject("http://localhost:8083/payments", paymentRequest, Map.class);
    }

    private void reserveInventory(Order order) {
        Map<String, Object> inventoryRequest = new HashMap<>();
        inventoryRequest.put("orderId", order.getId());
        inventoryRequest.put("productId", order.getProductId());
        inventoryRequest.put("quantity", order.getQuantity());
        restTemplate.postForObject("http://localhost:8088/inventory/reserve", inventoryRequest, Map.class);
    }

    private void compensate(Order order) {
        try {
            restTemplate.postForObject(
                "http://localhost:8083/payments/refund/" + order.getId(), null, Void.class);
            log.info("Compensation: Payment refunded for order {}", order.getId());
        } catch (Exception e) {
            log.error("Compensation failed - refund: {}", e.getMessage());
        }
        try {
            restTemplate.postForObject(
                "http://localhost:8088/inventory/release/" + order.getId(), null, Void.class);
            log.info("Compensation: Inventory released for order {}", order.getId());
        } catch (Exception e) {
            log.error("Compensation failed - inventory release: {}", e.getMessage());
        }
        order.setStatus("COMPENSATED");
    }

    public Order getOrder(Long id) {
        return orderStore.get(id);
    }

    public java.util.Collection<Order> getAllOrders() {
        return orderStore.values();
    }
}
