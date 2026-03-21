package com.portal.user.service;

import com.portal.user.model.User;
import com.portal.user.repository.UserStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User Service - Business logic layer for user management operations.
 * <p>
 * Acts as an intermediary between the REST/Page controllers and the
 * {@link UserStaticRepository}. Provides methods for CRUD operations
 * and querying users by department or status.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Service
public class UserService {

    /** Repository for accessing the in-memory user data store. */
    private final UserStaticRepository repository;

    /**
     * Constructs the UserService with the required repository dependency.
     *
     * @param repository the static repository for user data access
     */
    public UserService(UserStaticRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all {@link User} objects
     */
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the user ID
     * @return an {@link Optional} containing the user if found
     */
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    /**
     * Creates a new user.
     *
     * @param user the user data to create
     * @return the created user with an auto-generated ID
     */
    public User createUser(User user) {
        return repository.save(user);
    }

    /**
     * Updates an existing user by ID.
     *
     * @param id   the ID of the user to update
     * @param user the updated user data
     * @return the updated user
     */
    public User updateUser(Long id, User user) {
        user.setId(id);
        return repository.save(user);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the user ID to delete
     * @return {@code true} if the user was deleted, {@code false} if not found
     */
    public boolean deleteUser(Long id) {
        return repository.deleteById(id);
    }

    /**
     * Retrieves all users belonging to a specific department.
     *
     * @param department the department name to filter by
     * @return a list of users in the specified department
     */
    public List<User> getUsersByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    /**
     * Retrieves all users with a specific account status.
     *
     * @param status the status to filter by (e.g., ACTIVE, INACTIVE)
     * @return a list of users with the specified status
     */
    public List<User> getUsersByStatus(String status) {
        return repository.findByStatus(status);
    }
}

