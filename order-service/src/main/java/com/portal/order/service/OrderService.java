package com.portal.order.service;

import com.portal.order.model.Order;
import com.portal.order.repository.OrderStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderStaticRepository repository;

    public OrderService(OrderStaticRepository repository) {
        this.repository = repository;
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return repository.findById(id);
    }

    public Order createOrder(Order order) {
        return repository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        order.setId(id);
        return repository.save(order);
    }

    public boolean deleteOrder(Long id) {
        return repository.deleteById(id);
    }

    public List<Order> getOrdersByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}

