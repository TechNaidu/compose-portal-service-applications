package com.portal.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gateway Service Application - Entry point for the API Gateway microservice.
 * <p>
 * This service acts as the central gateway and dashboard for the Compose Portal Application.
 * It provides a unified UI to monitor and navigate to all registered microservices
 * (User Service, Product Service, Order Service) and performs health checks on each.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@SpringBootApplication
public class GatewayServiceApplication {

    /**
     * Main method to bootstrap the Gateway Service Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}

