package com.fpavel.salfaContacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.mapper.ContactMapper;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.security.JwtUtils;
import com.fpavel.salfaContacts.security.UserDetailsServiceImpl;
import com.fpavel.salfaContacts.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ContactService contactService;
    @MockitoBean
    private ContactMapper mapper;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Contact contact;
    private ContactDto contactDto;
    private ContactCreateDto contactCreateDto;

    @BeforeEach
    void setUp() {
        contact = new Contact(1L, "+71234567890", "test@example.com");
        contactDto = new ContactDto(1L, "+71234567890", "test@example.com");
        contactCreateDto = new ContactCreateDto(100L, "+71234567890", "test@example.com");
    }

    @Test
    void create_ShouldReturnContactDto_WhenValidInput() throws Exception {
        when(mapper.contactCreateDtoToContact(any(ContactCreateDto.class))).thenReturn(contact);
        when(contactService.create(anyLong(), any(Contact.class))).thenReturn(contact);
        when(mapper.contactToContactDto(any(Contact.class))).thenReturn(contactDto);

        mockMvc.perform(post("/contact/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDto.id()))
                .andExpect(jsonPath("$.phone").value(contactDto.phone()))
                .andExpect(jsonPath("$.email").value(contactDto.email()));

        verify(mapper).contactCreateDtoToContact(eq(contactCreateDto));
        verify(contactService).create(eq(contactCreateDto.clientId()), same(contact));
        verify(mapper).contactToContactDto(same(contact));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        ContactCreateDto invalidDto = new ContactCreateDto(100L, "", "invalid-email");

        mockMvc.perform(post("/contact/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contactService, mapper);
    }

    @Test
    void getById_ShouldReturnContactDto_WhenExists() throws Exception {
        when(contactService.getById(1L)).thenReturn(Optional.of(contact));
        when(mapper.contactToContactDto(contact)).thenReturn(contactDto);

        mockMvc.perform(get("/contact/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDto.id()))
                .andExpect(jsonPath("$.phone").value(contactDto.phone()))
                .andExpect(jsonPath("$.email").value(contactDto.email()));

        verify(contactService).getById(1L);
        verify(mapper).contactToContactDto(contact);
    }

    @Test
    void getById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(contactService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/contact/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(contactService).getById(99L);
        verifyNoInteractions(mapper);
    }

    @Test
    void getAll_ShouldReturnListOfContactDto() throws Exception {
        List<Contact> contacts = List.of(contact);
        when(contactService.getAll()).thenReturn(contacts);
        when(mapper.contactToContactDto(contact)).thenReturn(contactDto);

        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(contact.getId()))
                .andExpect(jsonPath("$[0].phone").value(contact.getPhone()))
                .andExpect(jsonPath("$[0].email").value(contact.getEmail()));

        verify(contactService).getAll();
        verify(mapper).contactToContactDto(contact);
    }

    @Test
    void update_ShouldReturnUpdatedContactDto_WhenValidInput() throws Exception {
        when(mapper.contactDtoToContact(any(ContactDto.class))).thenReturn(contact);
        when(contactService.update(any(Contact.class))).thenReturn(contact);
        when(mapper.contactToContactDto(any(Contact.class))).thenReturn(contactDto);

        mockMvc.perform(patch("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDto.id()))
                .andExpect(jsonPath("$.phone").value(contactDto.phone()))
                .andExpect(jsonPath("$.email").value(contactDto.email()));

        verify(mapper).contactDtoToContact(eq(contactDto));
        verify(contactService).update(same(contact));
        verify(mapper).contactToContactDto(same(contact));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        ContactDto invalidDto = new ContactDto(1L, "", "invalid-email");

        mockMvc.perform(patch("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contactService, mapper);
    }

    @Test
    void delete_ShouldReturnOk_WhenDeleted() throws Exception {
        doNothing().when(contactService).delete(1L);

        mockMvc.perform(delete("/contact")
                        .param("contactId", "1"))
                .andExpect(status().isOk());

        verify(contactService).delete(1L);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenContactNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Contact not found")).when(contactService).delete(99L);

        mockMvc.perform(delete("/contact")
                        .param("contactId", "99"))
                .andExpect(status().isNotFound());

        verify(contactService).delete(99L);
    }
}