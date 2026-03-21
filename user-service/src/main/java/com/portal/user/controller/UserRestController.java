package com.portal.user.controller;

import com.portal.user.model.User;
import com.portal.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User REST Controller - Exposes RESTful API endpoints for user management.
 * <p>
 * Provides full CRUD operations (GET, POST, PUT, DELETE) for User entities,
 * plus filtering endpoints by department and account status.
 * All responses use JSON format.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    /** Service layer for user business logic. */
    private final UserService userService;

    /**
     * Constructs the UserRestController with the required service dependency.
     *
     * @param userService the service for user operations
     */
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a {@link ResponseEntity} containing a list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a specific user by their unique identifier.
     *
     * @param id the user ID
     * @return a {@link ResponseEntity} with the user if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user the user data from the request body
     * @return a {@link ResponseEntity} containing the created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id   the ID of the user to update
     * @param user the updated user data from the request body
     * @return a {@link ResponseEntity} containing the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the user ID to delete
     * @return a {@link ResponseEntity} with 200 OK if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all users in a specific department.
     *
     * @param department the department name to filter by
     * @return a {@link ResponseEntity} containing the filtered list of users
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(userService.getUsersByDepartment(department));
    }

    /**
     * Retrieves all users with a specific account status.
     *
     * @param status the status to filter by (e.g., ACTIVE, INACTIVE)
     * @return a {@link ResponseEntity} containing the filtered list of users
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(userService.getUsersByStatus(status));
    }
}

