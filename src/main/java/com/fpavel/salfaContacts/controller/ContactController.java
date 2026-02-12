package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.ContactCreateDto;
import com.fpavel.salfaContacts.dto.ContactDto;
import com.fpavel.salfaContacts.model.Contact;
import com.fpavel.salfaContacts.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового контакта клиента в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются имя и фамилия клиента")
    )
    public ResponseEntity<Contact> create(@RequestBody ContactCreateDto contactCreateDto) {
        return ResponseEntity.ok(service.create(contactCreateDto));
    }

    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Contact> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }


    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление контакта",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные клиента")
    )
    public ResponseEntity<Contact> update(@RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(service.update(contactDto));
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление контакта")
    public ResponseEntity<Void> delete(@Parameter(description = "Id контакта для удаления", required = true, example = "1")
                                       @RequestParam Long contactId) {
        service.delete(contactId);
        return ResponseEntity.ok().build();
    }
}

