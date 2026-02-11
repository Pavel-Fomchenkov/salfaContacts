package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;

import java.util.List;

public interface ContactService {
    Contact create(ContactCreateDto contactCreateDto);

    Contact getById(long id);

    List<Contact> getAll();

    Contact update(ContactDto contactDto);

    void delete(Long id);
}
