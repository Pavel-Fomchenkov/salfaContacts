package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact create(Long clientId, Contact contact);

    Optional<Contact> getById(long id);

    List<Contact> getAll();

    Contact update(Contact contact);

    void delete(Long id);
}
