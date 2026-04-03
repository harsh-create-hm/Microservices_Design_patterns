package com.example.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private String customerId;
    private Long productId;
    private Integer quantity;
    private Double amount;
    private String eventType;  // ORDER_CREATED, ORDER_CONFIRMED, ORDER_FAILED
}
