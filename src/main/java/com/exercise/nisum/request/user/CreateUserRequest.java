package com.exercise.nisum.request.user;

import com.exercise.nisum.request.phone.CreatePhoneRequest;
import com.exercise.nisum.request.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String name,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El correo no es válido")
        String email,
        @NotBlank(message = "El password es obligatorio")
        @ValidPassword
        String password,

        List<CreatePhoneRequest> phones
) { }
