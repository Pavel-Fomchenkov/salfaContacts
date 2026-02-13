package com.fpavel.salfaContacts.repository;

import com.fpavel.salfaContacts.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);

    Optional<Client> findById(Long id);

    Optional<Client> findByContactId(Long contactId);

    List<Client> findAll();

    void deleteById(Long id);
}
