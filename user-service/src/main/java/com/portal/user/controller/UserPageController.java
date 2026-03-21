package com.portal.user.controller;

import com.portal.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User Page Controller - Serves Thymeleaf-based UI pages for user management.
 * <p>
 * Provides the home page, users listing page, and individual user detail page
 * rendered using server-side Thymeleaf templates.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Controller
public class UserPageController {

    /** Service layer for user business logic. */
    private final UserService userService;

    /**
     * Constructs the UserPageController with the required service dependency.
     *
     * @param userService the service for user operations
     */
    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Renders the User Service home page with all users listed.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "index"})
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("serviceName", "User Service");
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    /**
     * Renders the users listing page displaying all users in a table.
     *
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "users"})
     */
    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    /**
     * Renders the detail page for a specific user identified by ID.
     *
     * @param id    the unique identifier of the user to display
     * @param model the Spring MVC model to pass attributes to the view
     * @return the name of the Thymeleaf template ({@code "user-detail"})
     */
    @GetMapping("/users/{id}")
    public String userDetailPage(@PathVariable Long id, Model model) {
        userService.getUserById(id).ifPresent(user -> model.addAttribute("user", user));
        return "user-detail";
    }
}

