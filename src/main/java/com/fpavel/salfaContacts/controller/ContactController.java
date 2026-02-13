package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.mapper.ContactMapper;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService service;
    private final ContactMapper mapper;

    public ContactController(ContactService service, ContactMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового контакта клиента в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются id клиента, телефон и email")
    )
    public ResponseEntity<ContactDto> create(@Valid @RequestBody ContactCreateDto contactCreateDto) {
        Long clientId = contactCreateDto.clientId();
        Contact newData = mapper.contactCreateDtoToContact(contactCreateDto);
        Contact result = service.create(clientId, newData);
        return ResponseEntity.ok(mapper.contactToContactDto(result));
    }

    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Получение контакта по id",
            description = "Требуется id контакта"
    )
    public ResponseEntity<ContactDto> getById(@PathVariable long id) {
        Contact result = service.getById(id).orElseThrow(() -> new EntityNotFoundException("Contact id " + id + " not found"));
        return ResponseEntity.ok(mapper.contactToContactDto(result));
    }

    @GetMapping
    @Operation(summary = "Получение списка контактов")
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление контакта",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные контакта")
    )
    public ResponseEntity<ContactDto> update(@Valid @RequestBody ContactDto contactDto) {
        Contact newData = mapper.contactDtoToContact(contactDto);
        Contact result = service.update(newData);
        return ResponseEntity.ok(mapper.contactToContactDto(result));
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление контакта по id",
            description = "Требуется id контакта"
    )
    public ResponseEntity<Void> delete(@Parameter(description = "Id контакта для удаления", required = true, example = "1")
                                       @RequestParam Long contactId) {
        service.delete(contactId);
        return ResponseEntity.ok().build();
    }
}

