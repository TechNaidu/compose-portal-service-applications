package com.portal.product.service;

import com.portal.product.model.Product;
import com.portal.product.repository.ProductStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductStaticRepository repository;

    public ProductService(ProductStaticRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return repository.save(product);
    }

    public boolean deleteProduct(Long id) {
        return repository.deleteById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> getProductsByStatus(String status) {
        return repository.findByStatus(status);
    }
}

