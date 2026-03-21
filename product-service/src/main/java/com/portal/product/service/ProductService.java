package com.portal.product.service;

import com.portal.product.model.Product;
import com.portal.product.repository.ProductStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Product Service - Business logic layer for product catalog operations.
 * <p>
 * Acts as an intermediary between the REST/Page controllers and the
 * {@link ProductStaticRepository}. Provides methods for CRUD operations
 * and querying products by category or availability status.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Service
public class ProductService {

    /** Repository for accessing the in-memory product data store. */
    private final ProductStaticRepository repository;

    /**
     * Constructs the ProductService with the required repository dependency.
     *
     * @param repository the static repository for product data access
     */
    public ProductService(ProductStaticRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all {@link Product} objects
     */
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the product ID
     * @return an {@link Optional} containing the product if found
     */
    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    /**
     * Creates a new product.
     *
     * @param product the product data to create
     * @return the created product with an auto-generated ID
     */
    public Product createProduct(Product product) {
        return repository.save(product);
    }

    /**
     * Updates an existing product by ID.
     *
     * @param id      the ID of the product to update
     * @param product the updated product data
     * @return the updated product
     */
    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return repository.save(product);
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the product ID to delete
     * @return {@code true} if the product was deleted, {@code false} if not found
     */
    public boolean deleteProduct(Long id) {
        return repository.deleteById(id);
    }

    /**
     * Retrieves all products belonging to a specific category.
     *
     * @param category the category name to filter by
     * @return a list of products in the specified category
     */
    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategory(category);
    }

    /**
     * Retrieves all products with a specific availability status.
     *
     * @param status the status to filter by (e.g., AVAILABLE, OUT_OF_STOCK)
     * @return a list of products with the specified status
     */
    public List<Product> getProductsByStatus(String status) {
        return repository.findByStatus(status);
    }
}

