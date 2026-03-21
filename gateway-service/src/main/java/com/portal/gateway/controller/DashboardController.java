package com.portal.gateway.controller;

import com.portal.gateway.service.ServiceRegistryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard Controller - Serves Thymeleaf-based UI pages for the Gateway dashboard.
 * <p>
 * Provides the main landing page with an overview of all registered microservices,
 * a detailed services page, and a static about page.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Controller
public class DashboardController {

    /** Service registry used to discover and retrieve all registered microservices. */
    private final ServiceRegistryService serviceRegistryService;

    /**
     * Constructs the DashboardController with the required service registry dependency.
     *
     * @param serviceRegistryService the service responsible for managing registered services
     */
    public DashboardController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    /**
     * Renders the main dashboard page showing all registered microservices and their health status.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "dashboard"})
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("services", serviceRegistryService.getAllServices());
        return "dashboard";
    }

    /**
     * Renders the services listing page with detailed information about each microservice.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "services"})
     */
    @GetMapping("/services")
    public String servicesPage(Model model) {
        model.addAttribute("services", serviceRegistryService.getAllServices());
        return "services";
    }

    /**
     * Renders the static about page describing the Compose Portal Application.
     *
     * @return the name of the Thymeleaf template ({@code "about"})
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}

