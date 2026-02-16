package com.fpavel.salfaContacts.controller;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.dto.UserPasswordDto;
import com.fpavel.salfaContacts.mapper.UserMapper;
import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // CREATE
    @PostMapping("/new")
    @Operation(summary = "Добавление нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются логин и пароль пользователя")
    )
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        User newData = mapper.userCreateDtoToUser(userCreateDto);
        User result = service.create(newData);
        return ResponseEntity.ok(mapper.userToUserDto(result));
    }


    // READ
    @GetMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Получение пользователя по id",
            description = "Требуется id пользователя"
    )
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        User result = service.getById(id).orElseThrow(() -> new EntityNotFoundException("User id " + id + " not found"));
        return ResponseEntity.ok(mapper.userToUserDto(result));
    }

    @GetMapping
    @Operation(summary = "Получение списка пользователей")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> result = service.getAll().stream().map(mapper::userToUserDto).toList();
        return ResponseEntity.ok(result);
    }

    // UPDATE
    @PatchMapping
    @Operation(summary = "Обновление данных пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются все данные клиента, кроме пароля. Для смены пароля используйте отдельный метод.")
    )
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto) {
        User newData = mapper.userDtoToUser(userDto);
        UserDto result = mapper.userToUserDto(service.update(newData));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/password")
    @Operation(summary = "Изменение пароля пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Требуются id и пароль пользователя")
    )
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody UserPasswordDto userPasswordDto) {
        User newData = mapper.userPasswordDtoToUser(userPasswordDto);
        UserDto result = mapper.userToUserDto(service.changePassword(newData));
        return ResponseEntity.ok(result);
    }

    // DELETE
    @DeleteMapping()
    @Operation(summary = "Удаление пользователя по id",
            description = "Требуется id пользователя"
    )
    public ResponseEntity<Void> delete(@Parameter(description = "Id пользователя для удаления", required = true, example = "1")
                                       @RequestParam Long userId) {
        service.delete(userId);
        return ResponseEntity.ok().build();
    }
}
