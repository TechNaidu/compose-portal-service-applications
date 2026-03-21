package com.portal.gateway.service;

import com.portal.gateway.model.ServiceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRegistryService {

    @Value("${services.user-service-url}")
    private String userServiceUrl;

    @Value("${services.product-service-url}")
    private String productServiceUrl;

    @Value("${services.order-service-url}")
    private String orderServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

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

    private String checkHealth(String serviceUrl) {
        try {
            restTemplate.getForObject(serviceUrl + "/actuator/health", String.class);
            return "UP";
        } catch (Exception e) {
            return "DOWN";
        }
    }
}

