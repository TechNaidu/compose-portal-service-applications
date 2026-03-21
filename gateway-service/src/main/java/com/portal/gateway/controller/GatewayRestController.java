package com.portal.gateway.controller;

import com.portal.gateway.model.ServiceInfo;
import com.portal.gateway.service.ServiceRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*")
public class GatewayRestController {

    private final ServiceRegistryService serviceRegistryService;

    public GatewayRestController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceInfo>> getAllServices() {
        return ResponseEntity.ok(serviceRegistryService.getAllServices());
    }

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

