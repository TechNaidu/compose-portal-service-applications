package com.portal.product.repository;

import com.portal.product.model.Product;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Product Static Repository - In-memory data store for Product entities.
 * <p>
 * Uses a {@link ConcurrentHashMap} to simulate a database with pre-loaded static product data.
 * Supports CRUD operations and filtering by category or availability status.
 * Intended for development, testing, and demo purposes without requiring a real database.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Repository
public class ProductStaticRepository {

    /** Thread-safe in-memory store mapping product IDs to Product objects. */
    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();

    /** Atomic counter for generating unique IDs for new products. */
    private final AtomicLong idGenerator = new AtomicLong(100);

    /**
     * Initializes the repository with pre-loaded sample product data.
     * Called automatically after bean construction by the Spring container.
     */
    @PostConstruct
    public void init() {
        productStore.put(1L, new Product(1L, "MacBook Pro 16\"", "Apple M3 Pro chip, 18GB RAM, 512GB SSD", "Electronics", 2499.99, 50, "https://via.placeholder.com/150?text=MacBook", "AVAILABLE"));
        productStore.put(2L, new Product(2L, "iPhone 15 Pro", "A17 Pro chip, 256GB, Titanium", "Electronics", 1199.99, 120, "https://via.placeholder.com/150?text=iPhone", "AVAILABLE"));
        productStore.put(3L, new Product(3L, "Samsung 4K Smart TV 65\"", "Crystal UHD, HDR10+, Smart Hub", "Electronics", 799.99, 30, "https://via.placeholder.com/150?text=TV", "AVAILABLE"));
        productStore.put(4L, new Product(4L, "Nike Air Max 270", "Men's running shoes, breathable mesh", "Footwear", 149.99, 200, "https://via.placeholder.com/150?text=Nike", "AVAILABLE"));
        productStore.put(5L, new Product(5L, "Sony WH-1000XM5", "Wireless noise cancelling headphones", "Audio", 349.99, 80, "https://via.placeholder.com/150?text=Sony", "AVAILABLE"));
        productStore.put(6L, new Product(6L, "Ergonomic Office Chair", "Lumbar support, adjustable height, mesh back", "Furniture", 299.99, 45, "https://via.placeholder.com/150?text=Chair", "AVAILABLE"));
        productStore.put(7L, new Product(7L, "Kindle Paperwhite", "6.8\" display, waterproof, 16GB", "Electronics", 139.99, 150, "https://via.placeholder.com/150?text=Kindle", "AVAILABLE"));
        productStore.put(8L, new Product(8L, "Instant Pot Duo 7-in-1", "Electric pressure cooker, 6 quart", "Kitchen", 89.99, 60, "https://via.placeholder.com/150?text=InstantPot", "AVAILABLE"));
        productStore.put(9L, new Product(9L, "Levi's 501 Original Jeans", "Classic straight fit, medium wash", "Clothing", 69.99, 300, "https://via.placeholder.com/150?text=Levis", "AVAILABLE"));
        productStore.put(10L, new Product(10L, "PlayStation 5", "Digital Edition, 825GB SSD", "Gaming", 449.99, 0, "https://via.placeholder.com/150?text=PS5", "OUT_OF_STOCK"));
    }

    /**
     * Retrieves all products from the in-memory store.
     *
     * @return a list of all {@link Product} objects
     */
    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    /**
     * Finds a product by its unique identifier.
     *
     * @param id the product ID to look up
     * @return an {@link Optional} containing the product if found, or empty otherwise
     */
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }

    /**
     * Saves a product to the store. If the product has no ID, a new one is auto-generated.
     *
     * @param product the product to save
     * @return the saved product with its ID set
     */
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.incrementAndGet());
        }
        productStore.put(product.getId(), product);
        return product;
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the product ID to delete
     * @return {@code true} if the product was found and removed, {@code false} otherwise
     */
    public boolean deleteById(Long id) {
        return productStore.remove(id) != null;
    }

    /**
     * Finds all products belonging to a specific category (case-insensitive).
     *
     * @param category the category name to filter by
     * @return a list of products in the specified category
     */
    public List<Product> findByCategory(String category) {
        return productStore.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    /**
     * Finds all products with a specific availability status (case-insensitive).
     *
     * @param status the status to filter by (e.g., AVAILABLE, OUT_OF_STOCK)
     * @return a list of products with the specified status
     */
    public List<Product> findByStatus(String status) {
        return productStore.values().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}

