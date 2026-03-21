package com.portal.order.service;

import com.portal.order.model.Order;
import com.portal.order.repository.OrderStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Order Service - Business logic layer for order management operations.
 * <p>
 * Acts as an intermediary between the REST/Page controllers and the
 * {@link OrderStaticRepository}. Provides methods for CRUD operations
 * and querying orders by status or user ID.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Service
public class OrderService {

    /** Repository for accessing the in-memory order data store. */
    private final OrderStaticRepository repository;

    /**
     * Constructs the OrderService with the required repository dependency.
     *
     * @param repository the static repository for order data access
     */
    public OrderService(OrderStaticRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of all {@link Order} objects
     */
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id the order ID
     * @return an {@link Optional} containing the order if found
     */
    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    /**
     * Creates a new order.
     *
     * @param order the order data to create
     * @return the created order with an auto-generated ID and order number
     */
    public Order createOrder(Order order) {
        return repository.save(order);
    }

    /**
     * Updates an existing order by ID.
     *
     * @param id    the ID of the order to update
     * @param order the updated order data
     * @return the updated order
     */
    public Order updateOrder(Long id, Order order) {
        order.setId(id);
        return repository.save(order);
    }

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id the order ID to delete
     * @return {@code true} if the order was deleted, {@code false} if not found
     */
    public boolean deleteOrder(Long id) {
        return repository.deleteById(id);
    }

    /**
     * Retrieves all orders with a specific fulfillment status.
     *
     * @param status the status to filter by (e.g., PENDING, SHIPPED, DELIVERED)
     * @return a list of orders with the specified status
     */
    public List<Order> getOrdersByStatus(String status) {
        return repository.findByStatus(status);
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the user ID to look up orders for
     * @return a list of orders belonging to the specified user
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}

