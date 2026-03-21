package com.portal.gateway.service;

import com.portal.gateway.model.ServiceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Registry Service - Manages the registry of downstream microservices.
 * <p>
 * Maintains a list of all known microservices (User, Product, Order) along with
 * their URLs, descriptions, and real-time health status. Health checks are
 * performed by calling each service's Spring Boot Actuator health endpoint.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Service
public class ServiceRegistryService {

    /** Base URL of the User Service, injected from application configuration. */
    @Value("${services.user-service-url}")
    private String userServiceUrl;

    /** Base URL of the Product Service, injected from application configuration. */
    @Value("${services.product-service-url}")
    private String productServiceUrl;

    /** Base URL of the Order Service, injected from application configuration. */
    @Value("${services.order-service-url}")
    private String orderServiceUrl;

    /** REST client used to perform health check HTTP calls to downstream services. */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Builds and returns the list of all registered microservices with their
     * current health status determined via actuator health endpoint calls.
     *
     * @return a list of {@link ServiceInfo} objects representing each registered service
     */
    public List<ServiceInfo> getAllServices() {
        List<ServiceInfo> services = new ArrayList<>();

        services.add(new ServiceInfo(
                "User Service",
                "Manage users, roles, and departments. Full CRUD operations with static data.",
                userServiceUrl,
                userServiceUrl + "/actuator/health",
                checkHealth(userServiceUrl),
                "🧑‍💼",
                "#1a73e8"
        ));

        services.add(new ServiceInfo(
                "Product Service",
                "Browse product catalog, categories, pricing, and inventory management.",
                productServiceUrl,
                productServiceUrl + "/actuator/health",
                checkHealth(productServiceUrl),
                "📦",
                "#00897b"
        ));

        services.add(new ServiceInfo(
                "Order Service",
                "Track customer orders, order items, shipping, and payment details.",
                orderServiceUrl,
                orderServiceUrl + "/actuator/health",
                checkHealth(orderServiceUrl),
                "📋",
                "#e65100"
        ));

        return services;
    }

    /**
     * Performs a health check on the given service by calling its actuator health endpoint.
     *
     * @param serviceUrl the base URL of the service to check
     * @return {@code "UP"} if the service responds successfully, {@code "DOWN"} otherwise
     */
    private String checkHealth(String serviceUrl) {
        try {
            restTemplate.getForObject(serviceUrl + "/actuator/health", String.class);
            return "UP";
        } catch (Exception e) {
            return "DOWN";
        }
    }
}

