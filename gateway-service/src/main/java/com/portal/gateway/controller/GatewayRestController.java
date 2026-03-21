package com.portal.gateway.controller;

import com.portal.gateway.model.ServiceInfo;
import com.portal.gateway.service.ServiceRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Gateway REST Controller - Exposes REST API endpoints for the API Gateway.
 * <p>
 * Provides endpoints to list all registered microservices and to check
 * the aggregated health status of the gateway and its downstream services.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*")
public class GatewayRestController {

    /** Service registry used to discover and query registered microservices. */
    private final ServiceRegistryService serviceRegistryService;

    /**
     * Constructs the GatewayRestController with the required service registry dependency.
     *
     * @param serviceRegistryService the service responsible for managing registered services
     */
    public GatewayRestController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    /**
     * Retrieves the list of all registered microservices with their metadata and health status.
     *
     * @return a {@link ResponseEntity} containing a list of {@link ServiceInfo} objects
     */
    @GetMapping("/services")
    public ResponseEntity<List<ServiceInfo>> getAllServices() {
        return ResponseEntity.ok(serviceRegistryService.getAllServices());
    }

    /**
     * Returns an aggregated health map showing the status of the gateway itself
     * and each downstream microservice.
     *
     * @return a {@link ResponseEntity} containing a map of service names to their health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> gatewayHealth() {
        List<ServiceInfo> services = serviceRegistryService.getAllServices();
        Map<String, String> healthMap = new java.util.LinkedHashMap<>();
        healthMap.put("gateway", "UP");
        for (ServiceInfo service : services) {
            healthMap.put(service.getName(), service.getStatus());
        }
        return ResponseEntity.ok(healthMap);
    }
}

