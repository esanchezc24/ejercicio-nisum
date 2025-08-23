package com.exercise.nisum.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String mensaje;
    private int codeStatus;
    private LocalDateTime fechaHora;

    public ErrorResponse(String mensaje, int codeStatus, LocalDateTime fechaHora) {
        this.mensaje = mensaje;
        this.codeStatus = codeStatus;
        this.fechaHora = fechaHora;
    }

}
