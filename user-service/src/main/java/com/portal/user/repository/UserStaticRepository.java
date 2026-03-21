package com.portal.user.repository;

import com.portal.user.model.User;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User Static Repository - In-memory data store for User entities.
 * <p>
 * Uses a {@link ConcurrentHashMap} to simulate a database with pre-loaded static data.
 * Supports CRUD operations and filtering by department or account status.
 * Intended for development, testing, and demo purposes without requiring a real database.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
@Repository
public class UserStaticRepository {

    /** Thread-safe in-memory store mapping user IDs to User objects. */
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    /** Atomic counter for generating unique IDs for new users. */
    private final AtomicLong idGenerator = new AtomicLong(100);

    /**
     * Initializes the repository with pre-loaded sample user data.
     * Called automatically after bean construction by the Spring container.
     */
    @PostConstruct
    public void init() {
        userStore.put(1L, new User(1L, "John", "Doe", "john.doe@portal.com", "ADMIN", "Engineering", "ACTIVE"));
        userStore.put(2L, new User(2L, "Jane", "Smith", "jane.smith@portal.com", "USER", "Marketing", "ACTIVE"));
        userStore.put(3L, new User(3L, "Bob", "Johnson", "bob.johnson@portal.com", "MANAGER", "Sales", "ACTIVE"));
        userStore.put(4L, new User(4L, "Alice", "Williams", "alice.williams@portal.com", "USER", "Engineering", "INACTIVE"));
        userStore.put(5L, new User(5L, "Charlie", "Brown", "charlie.brown@portal.com", "USER", "Support", "ACTIVE"));
        userStore.put(6L, new User(6L, "Diana", "Prince", "diana.prince@portal.com", "MANAGER", "HR", "ACTIVE"));
        userStore.put(7L, new User(7L, "Edward", "Norton", "edward.norton@portal.com", "USER", "Finance", "ACTIVE"));
        userStore.put(8L, new User(8L, "Fiona", "Apple", "fiona.apple@portal.com", "ADMIN", "Engineering", "ACTIVE"));
    }

    /**
     * Retrieves all users from the in-memory store.
     *
     * @return a list of all {@link User} objects
     */
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the user ID to look up
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    /**
     * Saves a user to the store. If the user has no ID, a new one is auto-generated.
     *
     * @param user the user to save
     * @return the saved user with its ID set
     */
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the user ID to delete
     * @return {@code true} if the user was found and removed, {@code false} otherwise
     */
    public boolean deleteById(Long id) {
        return userStore.remove(id) != null;
    }

    /**
     * Finds all users belonging to a specific department (case-insensitive).
     *
     * @param department the department name to filter by
     * @return a list of users in the specified department
     */
    public List<User> findByDepartment(String department) {
        return userStore.values().stream()
                .filter(u -> u.getDepartment().equalsIgnoreCase(department))
                .toList();
    }

    /**
     * Finds all users with a specific account status (case-insensitive).
     *
     * @param status the status to filter by (e.g., ACTIVE, INACTIVE)
     * @return a list of users with the specified status
     */
    public List<User> findByStatus(String status) {
        return userStore.values().stream()
                .filter(u -> u.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}

