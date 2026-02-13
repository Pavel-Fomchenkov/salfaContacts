package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.repository.ClientRepository;
import com.fpavel.salfaContacts.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @Override
    public Client create(Client client) {
        if (client.getId() != null) {
            throw new IllegalArgumentException("ID must not be specified");
        }
        if (client.getName() == null || client.getLastName() == null ||
                client.getName().isEmpty() || client.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Name and lastName cannot be null");
        }
        client.setContact(null);
        return repository.save(client);
    }

    // READ
    @Override
    @Transactional
    public Optional<Client> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Client> findByContactId(Long contactId) {
        return repository.findByContactId(contactId);
    }

    @Override
    public List<Client> getAll() {
        return repository.findAll();
    }

    // UPDATE
    @Override
    public Client update(Client client) {
        if (client.getId() == null || client.getName() == null || client.getLastName() == null ||
                client.getName().isEmpty() || client.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Client id must not be null, name and lastName cannot be null");
        }

        Client clientDb = getById(client.getId()).orElseThrow(() ->
                new EntityNotFoundException("Client id " + client.getId() + " not found"));

        clientDb.setName(client.getName());
        clientDb.setLastName(client.getLastName());
        return repository.save(clientDb);
    }

    // DELETE
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
