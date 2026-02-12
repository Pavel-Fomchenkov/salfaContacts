package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются логин и пароль пользователя")
    )
    public ResponseEntity<User> create(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(service.create(userCreateDto));
    }


    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление данных пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные клиента. Для смены пароля используйте отдельный метод.")
    )
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(service.update(userDto));
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление пользователя по id")
    public ResponseEntity<Void> delete(@Parameter(description = "Id пользователя для удаления", required = true, example = "1")
                                       @RequestParam Long userId) {
        service.delete(userId);
        return ResponseEntity.ok().build();
    }


}
