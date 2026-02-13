package com.fpavel.salfaContacts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientCreateDto(
        @NotBlank(message = "Имя не может быть null")
        @Pattern(regexp = "^[A-ZА-Я][a-zа-яё]{1,14}$", message = "Некорректный формат имени") String name,
        @NotBlank(message = "Фамилия не может быть null")
        @Pattern(regexp = "^[A-ZА-Я][a-zа-яё]{1,14}$", message = "Некорректный формат фамилии") String lastName) {
}
