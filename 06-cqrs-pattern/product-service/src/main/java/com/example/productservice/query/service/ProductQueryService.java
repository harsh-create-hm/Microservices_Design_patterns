package com.example.productservice.query.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.ReadOnly;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    @ReadOnly
    public List<Product> getAllProducts() {
        log.info("QUERY: Fetching all products");
        return productRepository.findAll();
    }

    @ReadOnly
    public Product getProductById(Long id) {
        log.info("QUERY: Fetching product {}", id);
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @ReadOnly
    public List<Product> searchByName(String name) {
        log.info("QUERY: Searching products by name '{}'", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
