package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.User;

public record UserDto(Long id, String login, User.Role role) {
}
