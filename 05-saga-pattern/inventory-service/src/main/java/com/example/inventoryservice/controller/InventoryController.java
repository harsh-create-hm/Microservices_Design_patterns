package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/reserve")
    public Map<String, Object> reserveInventory(@RequestBody Map<String, Object> request) {
        return inventoryService.reserveInventory(request);
    }

    @PostMapping("/release/{orderId}")
    public Map<String, Object> releaseInventory(@PathVariable Long orderId) {
        return inventoryService.releaseInventory(orderId);
    }

    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Long productId) {
        return inventoryService.getInventory(productId);
    }
}
