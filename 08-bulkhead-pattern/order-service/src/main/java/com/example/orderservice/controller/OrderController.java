package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> request) {
        return orderService.processOrder(request);
    }

    @GetMapping
    public Map<Long, Map<String, Object>> getAllOrders() {
        return orderService.getAllOrders();
    }
}
