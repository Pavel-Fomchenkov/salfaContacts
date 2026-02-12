package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.User;

public record UserCreateDto(String login, String password, User.Role role) {
    public UserCreateDto(String login, String password) {
        this(login, password, User.Role.USER);
    }
}
