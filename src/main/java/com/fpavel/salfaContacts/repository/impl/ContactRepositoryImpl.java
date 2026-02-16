package com.fpavel.salfaContacts.repository.impl;

import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.repository.ContactRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepositoryImpl extends ContactRepository, JpaRepository<Contact, Long> {
}
