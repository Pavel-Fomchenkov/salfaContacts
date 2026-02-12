package com.fpavel.salfaContacts.service;

import com.fpavel.salfaContacts.dto.UserCreateDto;
import com.fpavel.salfaContacts.dto.UserDto;
import com.fpavel.salfaContacts.model.User;

import java.util.List;

public interface UserService {
    User create(UserCreateDto userCreateDto);

    UserDto getById(long id);

    List<UserDto> getAll();

    UserDto update(UserDto userDto);

    void delete(Long id);
}