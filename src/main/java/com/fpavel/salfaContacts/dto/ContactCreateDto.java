package com.fpavel.salfaContacts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ContactCreateDto(@NotNull(message = "ID клиента обязательно")
                               Long clientId,
                               @NotBlank(message = "Номер телефона не может быть null")
                               @Pattern(regexp = "^\\+[0-9]{10,15}$", message = "Некорректный номер телефона")
                               String phone,
                               @NotBlank(message = "Email не может быть null")
                               @Email(message = "Некорректный email")
                               String email) {
}
