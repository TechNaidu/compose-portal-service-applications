package com.portal.user.service;

import com.portal.user.model.User;
import com.portal.user.repository.UserStaticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserStaticRepository repository;

    public UserService(UserStaticRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public User updateUser(Long id, User user) {
        user.setId(id);
        return repository.save(user);
    }

    public boolean deleteUser(Long id) {
        return repository.deleteById(id);
    }

    public List<User> getUsersByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public List<User> getUsersByStatus(String status) {
        return repository.findByStatus(status);
    }
}

