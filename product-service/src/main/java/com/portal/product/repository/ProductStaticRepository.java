package com.portal.product.repository;

import com.portal.product.model.Product;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductStaticRepository {

    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(100);

    @PostConstruct
    public void init() {
        productStore.put(1L, new Product(1L, "MacBook Pro 16\"", "Apple M3 Pro chip, 18GB RAM, 512GB SSD", "Electronics", 2499.99, 50, "https://via.placeholder.com/150?text=MacBook", "AVAILABLE"));
        productStore.put(2L, new Product(2L, "iPhone 15 Pro", "A17 Pro chip, 256GB, Titanium", "Electronics", 1199.99, 120, "https://via.placeholder.com/150?text=iPhone", "AVAILABLE"));
        productStore.put(3L, new Product(3L, "Samsung 4K Smart TV 65\"", "Crystal UHD, HDR10+, Smart Hub", "Electronics", 799.99, 30, "https://via.placeholder.com/150?text=TV", "AVAILABLE"));
        productStore.put(4L, new Product(4L, "Nike Air Max 270", "Men's running shoes, breathable mesh", "Footwear", 149.99, 200, "https://via.placeholder.com/150?text=Nike", "AVAILABLE"));
        productStore.put(5L, new Product(5L, "Sony WH-1000XM5", "Wireless noise cancelling headphones", "Audio", 349.99, 80, "https://via.placeholder.com/150?text=Sony", "AVAILABLE"));
        productStore.put(6L, new Product(6L, "Ergonomic Office Chair", "Lumbar support, adjustable height, mesh back", "Furniture", 299.99, 45, "https://via.placeholder.com/150?text=Chair", "AVAILABLE"));
        productStore.put(7L, new Product(7L, "Kindle Paperwhite", "6.8\" display, waterproof, 16GB", "Electronics", 139.99, 150, "https://via.placeholder.com/150?text=Kindle", "AVAILABLE"));
        productStore.put(8L, new Product(8L, "Instant Pot Duo 7-in-1", "Electric pressure cooker, 6 quart", "Kitchen", 89.99, 60, "https://via.placeholder.com/150?text=InstantPot", "AVAILABLE"));
        productStore.put(9L, new Product(9L, "Levi's 501 Original Jeans", "Classic straight fit, medium wash", "Clothing", 69.99, 300, "https://via.placeholder.com/150?text=Levis", "AVAILABLE"));
        productStore.put(10L, new Product(10L, "PlayStation 5", "Digital Edition, 825GB SSD", "Gaming", 449.99, 0, "https://via.placeholder.com/150?text=PS5", "OUT_OF_STOCK"));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStore.get(id));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.incrementAndGet());
        }
        productStore.put(product.getId(), product);
        return product;
    }

    public boolean deleteById(Long id) {
        return productStore.remove(id) != null;
    }

    public List<Product> findByCategory(String category) {
        return productStore.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> findByStatus(String status) {
        return productStore.values().stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}

