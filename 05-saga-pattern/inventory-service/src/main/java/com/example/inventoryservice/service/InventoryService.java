package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class InventoryService {

    private final Map<Long, Inventory> inventoryStore = new ConcurrentHashMap<>();
    private final Map<Long, Map<String, Object>> reservations = new ConcurrentHashMap<>();

    public InventoryService() {
        inventoryStore.put(1L, new Inventory(1L, "Laptop", 50, 0));
        inventoryStore.put(2L, new Inventory(2L, "Mouse", 200, 0));
        inventoryStore.put(3L, new Inventory(3L, "Keyboard", 150, 0));
    }

    public Map<String, Object> reserveInventory(Map<String, Object> request) {
        Long orderId = Long.valueOf(request.get("orderId").toString());
        Long productId = Long.valueOf(request.get("productId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());

        Inventory inventory = inventoryStore.get(productId);
        if (inventory == null || inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient inventory for product: " + productId);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        reservations.put(orderId, request);

        log.info("Saga: Reserved {} units of product {} for order {}", quantity, productId, orderId);
        return Map.of("status", "RESERVED", "orderId", orderId, "productId", productId, "quantity", quantity);
    }

    public Map<String, Object> releaseInventory(Long orderId) {
        Map<String, Object> reservation = reservations.get(orderId);
        if (reservation != null) {
            Long productId = Long.valueOf(reservation.get("productId").toString());
            Integer quantity = Integer.valueOf(reservation.get("quantity").toString());
            Inventory inventory = inventoryStore.get(productId);
            if (inventory != null) {
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
                inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
                log.info("Saga compensation: Released {} units of product {} for order {}", quantity, productId, orderId);
            }
        }
        return Map.of("status", "RELEASED", "orderId", orderId);
    }

    public Inventory getInventory(Long productId) {
        return inventoryStore.get(productId);
    }
}
