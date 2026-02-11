package com.fpavel.salfaContacts.service.impl;

import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.mapper.ClientMapper;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.repository.ClientRepository;
import com.fpavel.salfaContacts.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public Client create(ClientCreateDto clientCreateDto) {
        Client newClient = new Client(clientCreateDto.name(), clientCreateDto.lastName());
        return repository.save(newClient);
    }

    // READ
    @Override
    public Client getById(long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Client> getAll() {
        return repository.findAll();
    }

    // UPDATE
    @Override
    public Client update(ClientDto clientDto) {
        return repository.save(mapper.clientDtoToClient(clientDto));
    }

    // DELETE
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
