package com.portal.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Product Service Application - Entry point for the Product Catalog microservice.
 * <p>
 * Provides REST APIs and Thymeleaf-based UI pages for managing products including
 * CRUD operations, category filtering, and inventory status tracking.
 * Uses an in-memory static data store (no real database required).
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@SpringBootApplication
public class ProductServiceApplication {

    /**
     * Main method to bootstrap the Product Service Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

