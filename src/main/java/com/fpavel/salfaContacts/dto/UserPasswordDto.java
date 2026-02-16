package com.fpavel.salfaContacts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPasswordDto(@NotNull(message = "ID пользователя обязательно")
                              Long id,
                              @NotBlank(message = "Пароль не может быть пустым")
                              @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]+$", message = "Пароль содержит недопустимые символы")
                              @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
                              String password) {
}
