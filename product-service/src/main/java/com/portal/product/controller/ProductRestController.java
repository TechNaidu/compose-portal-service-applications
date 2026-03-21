package com.portal.product.controller;

import com.portal.product.model.Product;
import com.portal.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product REST Controller - Exposes RESTful API endpoints for product catalog management.
 * <p>
 * Provides full CRUD operations (GET, POST, PUT, DELETE) for Product entities,
 * plus a filtering endpoint by category. All responses use JSON format.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductRestController {

    /** Service layer for product business logic. */
    private final ProductService productService;

    /**
     * Constructs the ProductRestController with the required service dependency.
     *
     * @param productService the service for product operations
     */
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products.
     *
     * @return a {@link ResponseEntity} containing a list of all products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param id the product ID
     * @return a {@link ResponseEntity} with the product if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new product.
     *
     * @param product the product data from the request body
     * @return a {@link ResponseEntity} containing the created product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    /**
     * Updates an existing product by ID.
     *
     * @param id      the ID of the product to update
     * @param product the updated product data from the request body
     * @return a {@link ResponseEntity} containing the updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the product ID to delete
     * @return a {@link ResponseEntity} with 200 OK if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all products in a specific category.
     *
     * @param category the category name to filter by
     * @return a {@link ResponseEntity} containing the filtered list of products
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
}

