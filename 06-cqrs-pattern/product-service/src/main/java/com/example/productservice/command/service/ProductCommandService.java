package com.example.productservice.command.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        log.info("COMMAND: Creating product '{}'", product.getName());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updated) {
        log.info("COMMAND: Updating product {}", id);
        Product existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("COMMAND: Deleting product {}", id);
        productRepository.deleteById(id);
    }
}
