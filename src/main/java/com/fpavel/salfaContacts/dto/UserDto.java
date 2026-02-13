package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDto(@NotNull(message = "ID пользователя обязательно") Long id,
                      @NotBlank(message = "Логин не может быть null")
                      @Pattern(
                              regexp = "^(?=.{5,20}$)[a-zA-Z0-9._-]+$",
                              message = "Логин должен быть длиной от 5 до 20 символов и содержать только латинские буквы, цифры, точки, подчёркивания и дефисы"
                      )
                      String login,
                      User.Role role) {
}
