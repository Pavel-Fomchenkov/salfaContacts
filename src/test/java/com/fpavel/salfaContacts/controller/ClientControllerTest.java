package com.fpavel.salfaContacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.dto.ClientNoContactDto;
import com.fpavel.salfaContacts.mapper.ClientMapper;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.security.JwtUtils;
import com.fpavel.salfaContacts.security.UserDetailsServiceImpl;
import com.fpavel.salfaContacts.service.ClientService;
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

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {
    @MockitoBean
    ClientService service;
    @MockitoBean
    private ClientMapper mapper;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Client client;
    private ClientDto clientDto;
    private ClientCreateDto clientCreateDto;
    private ClientNoContactDto clientNoContactDto;


    @BeforeEach
    void setUp() {
        client = new Client(1L, "Testclientname", "Testclientlastname");
        clientDto = new ClientDto(1L, "Testclientname", "Testclientlastname", new Contact());
        clientCreateDto = new ClientCreateDto("Testclientname", "Testclientlastname");
        clientNoContactDto = new ClientNoContactDto(1L, "Testclientname", "Testclientlastname");
    }

    @Test
    void create_ShouldCallMapperAndServiceWithCorrectArguments() throws Exception {
        when(mapper.clientCreateDtoToClient(clientCreateDto)).thenReturn(client);
        when(service.create(any(Client.class))).thenReturn(client);
        when(mapper.clientToClientDto(any(Client.class))).thenReturn(clientDto);

        mockMvc.perform(post("/client/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientCreateDto)))
                .andExpect(status().isOk());

        verify(service).create(same(client));
    }

    @Test
    void getById_ShouldReturnDto_WhenExisted() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(client));
        when(mapper.clientToClientDto(client)).thenReturn(clientDto);

        mockMvc.perform(get("/client/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientDto.id()))
                .andExpect(jsonPath("$.name").value(clientDto.name()))
                .andExpect(jsonPath("$.lastName").value(clientDto.lastName()));

        verify(service).getById(1L);
        verify(mapper).clientToClientDto(client);
    }

    @Test
    void getById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/client/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(service).getById(99L);
        verifyNoInteractions(mapper);
    }

    @Test
    void getAll_ShouldReturnListOfClientDto() throws Exception {
        List<Client> clients = List.of(client);
        List<ClientDto> clientDtos = List.of(clientDto);
        when(service.getAll()).thenReturn(clients);
        when(mapper.clientToClientDto(client)).thenReturn(clientDto);

        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(clientDto.id()));

        verify(service).getAll();
        verify(mapper, times(1)).clientToClientDto(client);
    }

    @Test
    void update_ShouldReturnUpdatedClient_WhenValidInput() throws Exception {
        when(mapper.clientNoContactDtoToClient(any(ClientNoContactDto.class))).thenReturn(client);
        when(service.update(any(Client.class))).thenReturn(client);
        when(mapper.clientToClientDto(any(Client.class))).thenReturn(clientDto);

        mockMvc.perform(patch("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientNoContactDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientDto.id()));

        verify(mapper).clientNoContactDtoToClient(any(ClientNoContactDto.class));
        verify(service).update(any(Client.class));
        verify(mapper).clientToClientDto(any(Client.class));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        ClientNoContactDto invalidDto = new ClientNoContactDto(1L, "", "");

        mockMvc.perform(patch("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service, mapper);
    }

    @Test
    void delete_ShouldReturnOk_WhenDeleted() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/client")
                        .param("contactId", "1"))
                .andExpect(status().isOk());

        verify(service).delete(1L);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenClientNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Client not found")).when(service).delete(99L);

        mockMvc.perform(delete("/client")
                        .param("contactId", "99"))
                .andExpect(status().isNotFound());

        verify(service).delete(99L);
    }
}