package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.repository.ContactRepository;
import com.fpavel.salfaContacts.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository repository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ContactServiceImpl contactService;

    private final Long clientId = 1L;
    private final Long contactId = 10L;
    private final String phone = "+123456789";
    private final String email = "test@example.com";

    @Test
    void create_ShouldSaveContactAndSetToClient_WhenValid() {
        Contact contactToCreate = new Contact(phone, email);
        Client client = mock(Client.class);

        when(clientService.getById(clientId)).thenReturn(Optional.of(client));
        when(repository.save(contactToCreate)).thenReturn(contactToCreate);

        Contact result = contactService.create(clientId, contactToCreate);

        assertSame(contactToCreate, result);
        verify(client).setContact(contactToCreate);
        verify(clientService).getById(clientId);
        verify(repository).save(contactToCreate);
    }

    @Test
    void create_ShouldThrowException_WhenClientIdNull() {
        Contact contactToCreate = new Contact(null, phone, email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(null, contactToCreate));
        assertEquals("Client id must not be null", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowException_WhenContactIdNotNull() {
        Contact contactToCreate = new Contact(contactId, phone, email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Contact id must not be specified", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowException_WhenPhoneNull() {
        Contact contactToCreate = new Contact(null, null, email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Phone number cannot be null", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowException_WhenPhoneEmpty() {
        Contact contactToCreate = new Contact(null, "", email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Phone number cannot be null", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowException_WhenEmailNull() {
        Contact contactToCreate = new Contact(null, phone, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Email cannot be null", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowException_WhenEmailEmpty() {
        Contact contactToCreate = new Contact(null, phone, "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Email cannot be null", exception.getMessage());
        verifyNoInteractions(clientService, repository);
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenClientNotFound() {
        Contact contactToCreate = new Contact(null, phone, email);
        when(clientService.getById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contactService.create(clientId, contactToCreate));
        assertEquals("Client id " + clientId + " not found", exception.getMessage());
        verify(clientService).getById(clientId);
        verifyNoInteractions(repository);
    }

    @Test
    void getById_ShouldReturnContact_WhenExists() {
        Contact contact = new Contact(contactId, phone, email);
        when(repository.findById(contactId)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.getById(contactId);

        assertTrue(result.isPresent());
        assertEquals(contact, result.get());
        verify(repository).findById(contactId);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenNotExists() {
        when(repository.findById(contactId)).thenReturn(Optional.empty());

        Optional<Contact> result = contactService.getById(contactId);

        assertFalse(result.isPresent());
        verify(repository).findById(contactId);
    }

    @Test
    void getAll_ShouldReturnListOfContacts() {
        List<Contact> contacts = List.of(
                new Contact(1L, "111", "a@b.com"),
                new Contact(2L, "222", "c@d.com")
        );
        when(repository.findAll()).thenReturn(contacts);

        List<Contact> result = contactService.getAll();

        assertEquals(contacts, result);
        verify(repository).findAll();
    }

    @Test
    void update_ShouldUpdatePhoneAndEmail_WhenValid() {
        Contact existingContact = new Contact(contactId, "oldPhone", "old@mail.com");
        Contact updateData = new Contact(contactId, phone, email);
        when(repository.findById(contactId)).thenReturn(Optional.of(existingContact));
        when(repository.save(existingContact)).thenReturn(existingContact);

        Contact result = contactService.update(updateData);

        assertEquals(existingContact, result);
        assertEquals(phone, existingContact.getPhone());
        assertEquals(email, existingContact.getEmail());
        verify(repository).findById(contactId);
        verify(repository).save(existingContact);
    }

    @Test
    void update_ShouldThrowException_WhenIdNull() {
        Contact updateData = new Contact(null, phone, email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.update(updateData));
        assertEquals("ID must not be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void update_ShouldThrowException_WhenPhoneNull() {
        Contact updateData = new Contact(contactId, null, email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.update(updateData));
        assertEquals("Phone number cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowException_WhenPhoneEmpty() {
        Contact updateData = new Contact(contactId, "", email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> contactService.update(updateData));
        assertEquals("Phone number cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenContactNotFound() {
        Contact updateData = new Contact(contactId, phone, email);
        when(repository.findById(contactId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contactService.update(updateData));
        assertEquals("Contact id " + contactId + " not found.", exception.getMessage());
        verify(repository).findById(contactId);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_ShouldRemoveContactFromClientAndDelete_WhenClientExists() {
        Client mockClient = mock(Client.class);
        when(clientService.findByContactId(contactId)).thenReturn(Optional.of(mockClient));

        contactService.delete(contactId);

        verify(mockClient).setContact(null);
        verify(clientService).findByContactId(contactId);
        verify(repository).deleteById(contactId);
    }

    @Test
    void delete_ShouldJustDeleteContact_WhenClientNotFound() {
        when(clientService.findByContactId(contactId)).thenReturn(Optional.empty());

        contactService.delete(contactId);

        verify(clientService).findByContactId(contactId);
        verify(repository).deleteById(contactId);
    }
}