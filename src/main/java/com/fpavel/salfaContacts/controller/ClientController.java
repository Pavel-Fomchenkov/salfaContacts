package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.dto.ClientNoContactDto;
import com.fpavel.salfaContacts.mapper.ClientMapper;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService service;
    private final ClientMapper mapper;

    public ClientController(ClientService service, ClientMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового клиента в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются имя и фамилия клиента")
    )
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientCreateDto clientCreateDto) {
        Client newData = mapper.clientCreateDtoToClient(clientCreateDto);
        Client result = service.create(newData);
        return ResponseEntity.ok(mapper.clientToClientDto(result));
    }

    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Получение клиента по id",
            description = "Требуется id клиента"
    )
    public ResponseEntity<ClientDto> getById(@PathVariable long id) {
        Client result = service.getById(id).orElseThrow(() -> new EntityNotFoundException("Client id " + id + " not found"));
        return ResponseEntity.ok(mapper.clientToClientDto(result));
    }

    @GetMapping
    @Operation(summary = "Получение списка клиентов")
    public ResponseEntity<List<ClientDto>> getAll() {
        List<ClientDto> result = service.getAll().stream().map(mapper::clientToClientDto).toList();
        return ResponseEntity.ok(result);
    }

    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление данных клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные клиента")
    )
    public ResponseEntity<ClientDto> update(@Valid @RequestBody ClientNoContactDto clientNoContactDto) {
        Client newData = mapper.clientNoContactDtoToClient(clientNoContactDto);
        ClientDto result = mapper.clientToClientDto(service.update(newData));
        return ResponseEntity.ok(result);
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление клиента по id",
            description = "Требуется id клиента"
    )
    public ResponseEntity<Void> delete(@Parameter(description = "Id клиента для удаления", required = true, example = "1")
                                       @RequestParam Long contactId) {
        service.delete(contactId);
        return ResponseEntity.ok().build();
    }
}
