package com.example.productservice.controller;

import com.example.productservice.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();

    public ProductController() {
        productStore.put(1L, new Product(1L, "Laptop", "High-performance laptop", 999.99));
        productStore.put(2L, new Product(2L, "Mouse", "Wireless mouse", 29.99));
        productStore.put(3L, new Product(3L, "Keyboard", "Mechanical keyboard", 79.99));
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return List.copyOf(productStore.values());
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        Product product = productStore.get(id);
        if (product == null) {
            throw new RuntimeException("Product not found: " + id);
        }
        return product;
    }
}
