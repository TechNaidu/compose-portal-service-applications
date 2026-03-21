package com.portal.product.model;

/**
 * Product - Model class representing a product in the catalog.
 * <p>
 * Contains product details including name, description, category, pricing,
 * stock quantity, image URL, and availability status.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
public class Product {

    /** Unique identifier for the product. */
    private Long id;

    /** Product display name. */
    private String name;

    /** Detailed description of the product. */
    private String description;

    /** Product category (e.g., Electronics, Footwear, Audio). */
    private String category;

    /** Unit price of the product in USD. */
    private Double price;

    /** Current stock quantity available in inventory. */
    private Integer stockQuantity;

    /** URL to the product image. */
    private String imageUrl;

    /** Product availability status: {@code "AVAILABLE"} or {@code "OUT_OF_STOCK"}. */
    private String status;

    /** Default no-arg constructor. */
    public Product() {}

    /**
     * Constructs a fully initialized Product instance.
     *
     * @param id            the unique product identifier
     * @param name          the product name
     * @param description   the product description
     * @param category      the product category
     * @param price         the unit price in USD
     * @param stockQuantity the available stock quantity
     * @param imageUrl      the product image URL
     * @param status        the availability status (AVAILABLE, OUT_OF_STOCK)
     */
    public Product(Long id, String name, String description, String category, Double price, Integer stockQuantity, String imageUrl, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

