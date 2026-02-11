package com.fpavel.salfaContacts.repository;

import com.fpavel.salfaContacts.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    Optional<Contact> findById(Long id);

    List<Contact> findAll();

    Contact save(Contact contact);

    void deleteById(Long id);
}
