package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientServiceImpl service;

    private final Long clientId = 1L;
    private final String name = "TestClientName";
    private final String lastName = "TestClientLastName";
    private final Contact contact = new Contact("+1111111111", "test@email.test");

    @Test
    void create_ShouldSaveClientWithoutContact_WhenValid() {
        Client clientToCreate = new Client(name, lastName);
        Client savedClient = new Client(clientId, name, lastName);
        clientToCreate.setContact(contact);
        when(repository.save(any(Client.class))).thenReturn(savedClient);

        Client result = service.create(clientToCreate);

        assertEquals(savedClient, result);
        verify(repository, times(1)).save(clientToCreate);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(lastName, result.getLastName());
        assertNull(result.getContact());
        assertNull(result.getContact());
    }

    @Test
    void create_ShouldThrowException_WhenIdNotNull() {
        Client clientToCreate = new Client(clientId, name, lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(clientToCreate));
        assertEquals("ID must not be specified", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void create_ShouldThrowException_WhenNameNull() {
        Client clientToCreate = new Client(null, lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(clientToCreate));
        assertEquals("Name and lastName cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void create_ShouldThrowException_WhenNameEmpty() {
        Client clientToCreate = new Client("", lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(clientToCreate));
        assertEquals("Name and lastName cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void create_ShouldThrowException_WhenLastNameNull() {
        Client clientToCreate = new Client(null, name, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(clientToCreate));
        assertEquals("Name and lastName cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void create_ShouldThrowException_WhenLastNameEmpty() {
        Client clientToCreate = new Client(name, "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(clientToCreate));
        assertEquals("Name and lastName cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void getById_ShouldReturnClient_WhenExists() {
        Client client = new Client(clientId, name, lastName);
        when(repository.findById(clientId)).thenReturn(Optional.of(client));

        Optional<Client> result = service.getById(clientId);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(repository, times(1)).findById(clientId);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenNotExists() {
        when(repository.findById(clientId)).thenReturn(Optional.empty());

        Optional<Client> result = service.getById(clientId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(clientId);
    }

    @Test
    void findByContactId_ShouldReturnClient_WhenExists() {
        Long contactId = 100L;
        Client client = new Client(clientId, name, lastName);
        when(repository.findByContactId(contactId)).thenReturn(Optional.of(client));

        Optional<Client> result = service.findByContactId(contactId);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(repository, times(1)).findByContactId(contactId);
    }

    @Test
    void findByContactId_ShouldReturnEmpty_WhenNotExists() {
        Long contactId = 100L;
        when(repository.findByContactId(contactId)).thenReturn(Optional.empty());

        Optional<Client> result = service.findByContactId(contactId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findByContactId(contactId);
    }

    @Test
    void getAll_ShouldReturnListOfClients() {
        List<Client> clients = List.of(
                new Client(1L, "TestClientName01", "TestClientLastName01"),
                new Client(2L, "TestClientName02", "TestClientLastName02")
        );
        when(repository.findAll()).thenReturn(clients);

        List<Client> result = service.getAll();

        assertEquals(clients, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void update_ShouldUpdateNameAndLastName_WhenValid() {
        Client existingClient = new Client(clientId, "OldName", "OldLastName");
        Client updateData = new Client(clientId, name, lastName);
        when(repository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(repository.save(existingClient)).thenReturn(existingClient);

        Client result = service.update(updateData);

        assertEquals(existingClient, result);
        assertEquals(name, existingClient.getName());
        assertEquals(lastName, existingClient.getLastName());

        verify(repository, times(1)).findById(clientId);
        verify(repository, times(1)).save(existingClient);
    }

    @Test
    void update_ShouldThrowException_WhenIdNull() {
        Client updateData = new Client(null, name, lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(updateData));
        assertEquals("Client id must not be null, name and lastName cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void update_ShouldThrowException_WhenNameNull() {
        Client updateData = new Client(clientId, null, lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(updateData));
        assertEquals("Client id must not be null, name and lastName cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowException_WhenNameEmpty() {
        Client updateData = new Client(clientId, "", lastName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(updateData));
        assertEquals("Client id must not be null, name and lastName cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowException_WhenLastNameNull() {
        Client updateData = new Client(clientId, name, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(updateData));
        assertEquals("Client id must not be null, name and lastName cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowException_WhenLastNameEmpty() {
        Client updateData = new Client(clientId, name, "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(updateData));
        assertEquals("Client id must not be null, name and lastName cannot be null", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenClientNotFound() {
        Client updateData = new Client(clientId, name, lastName);
        when(repository.findById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.update(updateData));
        assertEquals("Client id " + clientId + " not found", exception.getMessage());
        verify(repository, times(1)).findById(clientId);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        service.delete(clientId);

        verify(repository, times(1)).deleteById(clientId);
    }
}