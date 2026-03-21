package com.portal.order.controller;

import com.portal.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Order Page Controller - Serves Thymeleaf-based UI pages for order management.
 * <p>
 * Provides the home page, orders listing page, and individual order detail page
 * rendered using server-side Thymeleaf templates.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Controller
public class OrderPageController {

    /** Service layer for order business logic. */
    private final OrderService orderService;

    /**
     * Constructs the OrderPageController with the required service dependency.
     *
     * @param orderService the service for order operations
     */
    public OrderPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Renders the Order Service home page with all orders listed.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "index"})
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("serviceName", "Order Service");
        model.addAttribute("orders", orderService.getAllOrders());
        return "index";
    }

    /**
     * Renders the orders listing page displaying all orders in a table.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "orders"})
     */
    @GetMapping("/orders")
    public String ordersPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    /**
     * Renders the detail page for a specific order identified by ID.
     *
     * @param id    the unique identifier of the order to display
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "order-detail"})
     */
    @GetMapping("/orders/{id}")
    public String orderDetailPage(@PathVariable Long id, Model model) {
        orderService.getOrderById(id).ifPresent(order -> model.addAttribute("order", order));
        return "order-detail";
    }
}

