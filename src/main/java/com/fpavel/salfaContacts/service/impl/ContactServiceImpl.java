package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
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
    public Contact create(ContactCreateDto contactCreateDto) {
        Client client = clientService.getById(contactCreateDto.clientId());
        if (client.getContact() == null) return repository.save(mapper.contactCreateDtoToContact(contactCreateDto));
        throw new IllegalArgumentException("Client id " + contactCreateDto.clientId() + " already has contact info. To update contact data use UPDATE method.");
    }

    // READ
    @Override
    public Contact getById(long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Contact> getAll() {
        return repository.findAll();
    }

    // UPDATE
    @Override
    public Contact update(ContactDto contactDto) {
        return repository.save(mapper.contactDtoToContact(contactDto));
    }

    // DELETE
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
