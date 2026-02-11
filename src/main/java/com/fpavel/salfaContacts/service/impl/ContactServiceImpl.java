package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.mapper.ClientMapper;
import com.fpavel.salfaContacts.mapper.ContactMapper;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.repository.ContactRepository;
import com.fpavel.salfaContacts.service.ClientService;
import com.fpavel.salfaContacts.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final ContactMapper mapper;
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ContactServiceImpl(ContactRepository repository, ContactMapper mapper, ClientService clientService, ClientMapper clientMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    // CREATE
    @Override
    public Contact create(ContactCreateDto contactCreateDto) {
        Client client = clientService.getById(contactCreateDto.clientId());
        Contact newContact = mapper.contactCreateDtoToContact(contactCreateDto);
        newContact = repository.save(newContact);
        client.setContact(newContact);
        clientService.update(clientMapper.clientToClientDto(client));
        return newContact;
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
