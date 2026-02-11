package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.Contact;

public record ClientDto(Long id, String name, String lastName, Contact contact) {
}
