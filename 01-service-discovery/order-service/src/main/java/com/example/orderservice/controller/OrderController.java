package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final RestTemplate restTemplate;
    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        // Discover product-service by name via Eureka - no hardcoded URL
        Map<?, ?> product = restTemplate.getForObject(
            "http://product-service/products/" + order.getProductId(), Map.class);

        Double price = product != null ? (Double) product.get("price") : 0.0;
        order.setId(idGen.getAndIncrement());
        order.setStatus("CREATED");
        order.setTotalPrice(price * order.getQuantity());
        orderStore.put(order.getId(), order);
        return order;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return List.copyOf(orderStore.values());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        Order order = orderStore.get(id);
        if (order == null) {
            throw new RuntimeException("Order not found: " + id);
        }
        return order;
    }
}
