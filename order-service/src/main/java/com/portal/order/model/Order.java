package com.portal.order.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Order - Model class representing a customer order.
 * <p>
 * Contains order details including order number, customer information, line items,
 * total amount, fulfillment status, shipping address, payment method, and order date.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
public class Order {

    /** Unique identifier for the order. */
    private Long id;

    /** Human-readable order number (e.g., ORD-2026-001). */
    private String orderNumber;

    /** ID of the user who placed this order. */
    private Long userId;

    /** Full name of the customer who placed the order. */
    private String customerName;

    /** List of items included in this order. */
    private List<OrderItem> items;

    /** Total monetary amount for the order in USD. */
    private Double totalAmount;

    /** Current order status (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED). */
    private String status;

    /** Shipping address for order delivery. */
    private String shippingAddress;

    /** Payment method used (e.g., CREDIT_CARD, DEBIT_CARD, PAYPAL). */
    private String paymentMethod;

    /** Date and time when the order was placed. */
    private LocalDateTime orderDate;

    /** Default no-arg constructor. */
    public Order() {}

    /**
     * Constructs a fully initialized Order instance.
     *
     * @param id              the unique order identifier
     * @param orderNumber     the human-readable order number
     * @param userId          the ID of the user who placed the order
     * @param customerName    the customer's full name
     * @param items           the list of order line items
     * @param totalAmount     the total order amount in USD
     * @param status          the current order status
     * @param shippingAddress the shipping address
     * @param paymentMethod   the payment method used
     * @param orderDate       the date and time the order was placed
     */
    public Order(Long id, String orderNumber, Long userId, String customerName, List<OrderItem> items,
                 Double totalAmount, String status, String shippingAddress, String paymentMethod, LocalDateTime orderDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}

