package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    Optional<User> getById(long id);

    List<User> getAll();

    User update(User user);

    void delete(Long id);
    User changePassword(User user);
}