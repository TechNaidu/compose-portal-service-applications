package com.portal.order.model;

/**
 * OrderItem - Model class representing a single line item within an order.
 * <p>
 * Contains the product reference, quantity, unit price, and computed subtotal
 * for one product line in a customer's order.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
public class OrderItem {

    /** ID of the product being ordered. */
    private Long productId;

    /** Display name of the product. */
    private String productName;

    /** Quantity of the product ordered. */
    private Integer quantity;

    /** Unit price of the product at the time of ordering. */
    private Double unitPrice;

    /** Computed subtotal (quantity × unitPrice). */
    private Double subtotal;

    /** Default no-arg constructor. */
    public OrderItem() {}

    /**
     * Constructs a fully initialized OrderItem and computes the subtotal.
     *
     * @param productId   the ID of the product
     * @param productName the name of the product
     * @param quantity    the quantity ordered
     * @param unitPrice   the unit price at time of order
     */
    public OrderItem(Long productId, String productName, Integer quantity, Double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}

