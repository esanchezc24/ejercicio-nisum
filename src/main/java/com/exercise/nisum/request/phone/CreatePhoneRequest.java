package com.exercise.nisum.request.phone;

import jakarta.validation.constraints.NotBlank;

public record CreatePhoneRequest(
        @NotBlank(message = "El número es obligatorio")
        String number,
        @NotBlank(message = "El código de ciudad es obligatorio")
        String citycode,
        @NotBlank(message = "El código de país es obligatorio")
        String contrycode
) {
}
