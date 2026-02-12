package com.fpavel.salfaContacts.repository;

import com.fpavel.salfaContacts.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User client);

    Optional<User> findById(Long id);
    Optional<User> findByLogin(String login);

    List<User> findAll();

    void deleteById(Long id);
}
