package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.repository.UserRepository;
import com.fpavel.salfaContacts.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("ID must not be specified");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        if (user.getPasswordEncrypted() == null || user.getPasswordEncrypted().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return repository.save(user);
    }

    @Override
    public Optional<User> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }

        User userDb = repository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User id " + user.getId() + " not found."));
        userDb.setLogin(user.getLogin());
        userDb.setRole(user.getRole());
        return repository.save(userDb);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User changePassword(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (user.getPasswordEncrypted() == null) {
            throw new IllegalArgumentException("Password must not be null");
        }

        User userDb = repository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User id " + user.getId() + " not found."));
        userDb.setPasswordEncrypted(user.getPasswordEncrypted());

        return repository.save(userDb);
    }
}
