package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client create(Client client);

    Optional<Client> getById(long id);

    Optional<Client> findByContactId(Long contactId);

    List<Client> getAll();

    Client update(Client client);

    void delete(Long id);
}
