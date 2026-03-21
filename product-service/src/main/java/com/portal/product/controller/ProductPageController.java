package com.portal.product.controller;

import com.portal.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Product Page Controller - Serves Thymeleaf-based UI pages for product catalog browsing.
 * <p>
 * Provides the home page, products listing page, and individual product detail page
 * rendered using server-side Thymeleaf templates.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Controller
public class ProductPageController {

    /** Service layer for product business logic. */
    private final ProductService productService;

    /**
     * Constructs the ProductPageController with the required service dependency.
     *
     * @param productService the service for product operations
     */
    public ProductPageController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Renders the Product Service home page with all products listed.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "index"})
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("serviceName", "Product Service");
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    /**
     * Renders the products listing page displaying all products in a grid/table.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "products"})
     */
    @GetMapping("/products")
    public String productsPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    /**
     * Renders the detail page for a specific product identified by ID.
     *
     * @param id    the unique identifier of the product to display
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "product-detail"})
     */
    @GetMapping("/products/{id}")
    public String productDetailPage(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> model.addAttribute("product", product));
        return "product-detail";
    }
}

