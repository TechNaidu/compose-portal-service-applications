package com.portal.order.controller;

import com.portal.order.model.Order;
import com.portal.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order REST Controller - Exposes RESTful API endpoints for order management.
 * <p>
 * Provides full CRUD operations (GET, POST, PUT, DELETE) for Order entities,
 * plus filtering endpoints by order status and user ID.
 * All responses use JSON format.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderRestController {

    /** Service layer for order business logic. */
    private final OrderService orderService;

    /**
     * Constructs the OrderRestController with the required service dependency.
     *
     * @param orderService the service for order operations
     */
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     *
     * @return a {@link ResponseEntity} containing a list of all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Retrieves a specific order by its unique identifier.
     *
     * @param id the order ID
     * @return a {@link ResponseEntity} with the order if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new order.
     *
     * @param order the order data from the request body
     * @return a {@link ResponseEntity} containing the created order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    /**
     * Updates an existing order by ID.
     *
     * @param id    the ID of the order to update
     * @param order the updated order data from the request body
     * @return a {@link ResponseEntity} containing the updated order
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id the order ID to delete
     * @return a {@link ResponseEntity} with 200 OK if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrder(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all orders with a specific fulfillment status.
     *
     * @param status the status to filter by (e.g., PENDING, SHIPPED, DELIVERED)
     * @return a {@link ResponseEntity} containing the filtered list of orders
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the user ID to look up orders for
     * @return a {@link ResponseEntity} containing the user's orders
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}

