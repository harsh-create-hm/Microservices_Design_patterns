package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String customerId;
    private Double amount;
    private String status;  // PENDING, CONFIRMED, FAILED, COMPENSATED
}
