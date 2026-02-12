package com.fpavel.salfaContacts.repository.impl;

import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends UserRepository, JpaRepository<User, Long> {
}
