package com.fpavel.salfaContacts.mapper;

import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client clientDtoToClient(ClientDto dto) {
        return new Client(dto.id(), dto.name(), dto.lastName());
    }

    public ClientDto clientToClientDto(Client client) {
        return new ClientDto(client.getId(), client.getName(), client.getLastName(), client.getContact());
    }

    public Client clientCreateDtoToClient(ClientCreateDto dto) {
        return new Client(dto.name(), dto.lastName());
    }
}
