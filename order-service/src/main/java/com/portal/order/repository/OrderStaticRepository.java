package com.portal.order.repository;

import com.portal.order.model.Order;
import com.portal.order.model.OrderItem;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderStaticRepository {

    private final Map<Long, Order> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(100);

    @PostConstruct
    public void init() {
        orderStore.put(1L, new Order(1L, "ORD-2026-001", 1L, "John Doe",
                List.of(new OrderItem(1L, "MacBook Pro 16\"", 1, 2499.99),
                        new OrderItem(5L, "Sony WH-1000XM5", 1, 349.99)),
                2849.98, "DELIVERED", "123 Main St, New York, NY 10001", "CREDIT_CARD",
                LocalDateTime.of(2026, 1, 15, 10, 30)));

        orderStore.put(2L, new Order(2L, "ORD-2026-002", 2L, "Jane Smith",
                List.of(new OrderItem(2L, "iPhone 15 Pro", 2, 1199.99)),
                2399.98, "SHIPPED", "456 Oak Ave, Los Angeles, CA 90001", "DEBIT_CARD",
                LocalDateTime.of(2026, 2, 10, 14, 15)));

        orderStore.put(3L, new Order(3L, "ORD-2026-003", 3L, "Bob Johnson",
                List.of(new OrderItem(4L, "Nike Air Max 270", 3, 149.99),
                        new OrderItem(9L, "Levi's 501 Original Jeans", 2, 69.99)),
                589.95, "PROCESSING", "789 Pine Rd, Chicago, IL 60601", "PAYPAL",
                LocalDateTime.of(2026, 3, 5, 9, 0)));

        orderStore.put(4L, new Order(4L, "ORD-2026-004", 5L, "Charlie Brown",
                List.of(new OrderItem(3L, "Samsung 4K Smart TV 65\"", 1, 799.99),
                        new OrderItem(6L, "Ergonomic Office Chair", 2, 299.99)),
                1399.97, "PENDING", "321 Elm St, Houston, TX 77001", "CREDIT_CARD",
                LocalDateTime.of(2026, 3, 18, 16, 45)));

        orderStore.put(5L, new Order(5L, "ORD-2026-005", 6L, "Diana Prince",
                List.of(new OrderItem(7L, "Kindle Paperwhite", 1, 139.99),
                        new OrderItem(8L, "Instant Pot Duo 7-in-1", 1, 89.99)),
                229.98, "DELIVERED", "654 Birch Ln, Phoenix, AZ 85001", "CREDIT_CARD",
                LocalDateTime.of(2026, 1, 28, 11, 20)));

        orderStore.put(6L, new Order(6L, "ORD-2026-006", 7L, "Edward Norton",
                List.of(new OrderItem(2L, "iPhone 15 Pro", 1, 1199.99),
                        new OrderItem(5L, "Sony WH-1000XM5", 2, 349.99)),
                1899.97, "CANCELLED", "987 Cedar Dr, Philadelphia, PA 19101", "PAYPAL",
                LocalDateTime.of(2026, 2, 22, 8, 10)));

        orderStore.put(7L, new Order(7L, "ORD-2026-007", 1L, "John Doe",
                List.of(new OrderItem(6L, "Ergonomic Office Chair", 1, 299.99)),
                299.99, "SHIPPED", "123 Main St, New York, NY 10001", "DEBIT_CARD",
                LocalDateTime.of(2026, 3, 12, 13, 30)));

        orderStore.put(8L, new Order(8L, "ORD-2026-008", 8L, "Fiona Apple",
                List.of(new OrderItem(1L, "MacBook Pro 16\"", 1, 2499.99),
                        new OrderItem(7L, "Kindle Paperwhite", 2, 139.99)),
                2779.97, "PROCESSING", "246 Maple Ave, San Antonio, TX 78201", "CREDIT_CARD",
                LocalDateTime.of(2026, 3, 20, 15, 0)));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orderStore.values());
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderStore.get(id));
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.incrementAndGet());
            order.setOrderNumber("ORD-2026-" + String.format("%03d", order.getId()));
        }
        orderStore.put(order.getId(), order);
        return order;
    }

    public boolean deleteById(Long id) {
        return orderStore.remove(id) != null;
    }

    public List<Order> findByStatus(String status) {
        return orderStore.values().stream()
                .filter(o -> o.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    public List<Order> findByUserId(Long userId) {
        return orderStore.values().stream()
                .filter(o -> o.getUserId().equals(userId))
                .toList();
    }
}

