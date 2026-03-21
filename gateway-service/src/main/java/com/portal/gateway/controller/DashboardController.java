package com.portal.gateway.controller;

import com.portal.gateway.service.ServiceRegistryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ServiceRegistryService serviceRegistryService;

    public DashboardController(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("services", serviceRegistryService.getAllServices());
        return "dashboard";
    }

    @GetMapping("/services")
    public String servicesPage(Model model) {
        model.addAttribute("services", serviceRegistryService.getAllServices());
        return "services";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}

