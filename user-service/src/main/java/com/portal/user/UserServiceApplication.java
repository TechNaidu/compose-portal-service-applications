package com.portal.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service Application - Entry point for the User Management microservice.
 * <p>
 * Provides REST APIs and Thymeleaf-based UI pages for managing users including
 * CRUD operations, department filtering, and status filtering.
 * Uses an in-memory static data store (no real database required).
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@SpringBootApplication
public class UserServiceApplication {

    /**
     * Main method to bootstrap the User Service Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

