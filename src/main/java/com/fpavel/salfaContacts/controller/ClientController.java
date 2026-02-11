package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.ClientCreateDto;
import com.fpavel.salfaContacts.dto.ClientDto;
import com.fpavel.salfaContacts.model.Client;
import com.fpavel.salfaContacts.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового клиента в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются имя и фамилия клиента")
    )
    public ResponseEntity<Client> create(@RequestBody ClientCreateDto clientCreateDto) {
        return ResponseEntity.ok(service.create(clientCreateDto));
    }

    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Client> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление данных клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные клиента")
    )
    public ResponseEntity<Client> update(@RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(service.update(clientDto));
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление клиента",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются id клиента")
    )
    public ResponseEntity<Void> delete(@RequestParam Long contactId) {
        service.delete(contactId);
        return ResponseEntity.ok().build();
    }
}
