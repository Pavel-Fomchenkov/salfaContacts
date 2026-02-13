package com.fpavel.salfaContacts.mapper;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public Contact contactCreateDtoToContact(ContactCreateDto dto) {
        return new Contact(dto.phone(), dto.email());
    }

    public Contact contactDtoToContact(ContactDto dto) {
        return new Contact(dto.id(), dto.phone(), dto.email());
    }

    public ContactDto contactToContactDto(Contact contact) {
        return new ContactDto(contact.getId(), contact.getPhone(), contact.getEmail());
    }
}
