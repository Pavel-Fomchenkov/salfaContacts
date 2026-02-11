package com.fpavel.salfaContacts.repository.impl;

import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.repository.ClientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepositoryImpl extends ClientRepository, JpaRepository<Client, Long> {
}
