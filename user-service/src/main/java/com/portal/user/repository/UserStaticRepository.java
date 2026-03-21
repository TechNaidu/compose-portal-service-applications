package com.portal.user.repository;

import com.portal.user.model.User;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserStaticRepository {

    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(100);

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

    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    public boolean deleteById(Long id) {
        return userStore.remove(id) != null;
    }

    public List<User> findByDepartment(String department) {
        return userStore.values().stream()
                .filter(u -> u.getDepartment().equalsIgnoreCase(department))
                .toList();
    }

    public List<User> findByStatus(String status) {
        return userStore.values().stream()
                .filter(u -> u.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}

