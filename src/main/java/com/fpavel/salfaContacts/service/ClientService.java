package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;

import java.util.List;

public interface ClientService {
    Client create(ClientCreateDto clientCreateDto);

    Client getById(long id);

    List<Client> getAll();

    Client update(ClientDto clientDto);

    void delete(Long id);
}
