package com.fpavel.salfaContacts.dto;

import com.fpavel.salfaContacts.model.Contact;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientDto(@NotNull(message = "ID клиента обязательно") Long id,
                        @NotBlank(message = "Имя не может быть null")
                        @Pattern(regexp = "^[A-ZА-Я][a-zа-яё]{1,14}$", message = "Некорректный формат имени") String name,
                        @NotBlank(message = "Фамилия не может быть null")
                        @Pattern(regexp = "^[A-ZА-Я][a-zа-яё]{1,14}$", message = "Некорректный формат фамилии") String lastName,
                        Contact contact) {
}
