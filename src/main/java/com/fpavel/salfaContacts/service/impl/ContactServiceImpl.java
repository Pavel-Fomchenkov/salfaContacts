package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.mapper.ContactMapper;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.repository.ContactRepository;
import com.fpavel.salfaContacts.service.ClientService;
import com.fpavel.salfaContacts.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;
    private final ClientService clientService;

    public ContactServiceImpl(ContactRepository repository, ContactMapper mapper, ClientService clientService) {
        this.repository = repository;
        this.mapper = mapper;
        this.clientService = clientService;
    }

    // CREATE
    @Override
    @Transactional
    public Contact create(Long clientId, Contact contact) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id must not be null");
        }
        if (contact.getId() != null) {
            throw new IllegalArgumentException("Contact id must not be specified");
        }
        if (contact.getPhone() == null || contact.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        if (contact.getEmail() == null || contact.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        Client client = clientService.getById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client id " + clientId + " not found"));
        client.setContact(contact);
        return repository.save(contact);
    }

    // READ
    @Override
    public Optional<Contact> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Contact> getAll() {
        return repository.findAll();
    }

    // UPDATE
    @Override
    public Contact update(Contact contact) {
        if (contact.getId() == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        if (contact.getPhone() == null || contact.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        Contact contactDb = repository.findById(contact.getId())
                .orElseThrow(() -> new EntityNotFoundException("Contact id " + contact.getId() + " not found."));
        contactDb.setPhone(contact.getPhone());
        contactDb.setEmail(contact.getEmail());
        return repository.save(contactDb);
    }

    // DELETE
    @Override
    public void delete(Long id) {
        Optional<Client> client = clientService.findByContactId(id);
        client.ifPresent(c -> c.setContact(null));
        repository.deleteById(id);
    }
}
