package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank(message = "Логин не может быть null")
        @Pattern(
                regexp = "^(?=.{5,25}$)[a-zA-Z0-9._-]+$",
                message = "Логин должен быть длиной от 5 до 20 символов и содержать только латинские буквы, цифры, точки, подчёркивания и дефисы"
        )
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]+$", message = "Пароль содержит недопустимые символы")
        @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
        String password,

        User.Role role) {
    public UserCreateDto(String login, String password) {
        this(login, password, User.Role.USER);
    }
}
