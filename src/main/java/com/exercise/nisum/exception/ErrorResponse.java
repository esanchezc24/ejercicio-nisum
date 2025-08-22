package com.exercise.nisum.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String mensaje;
    private int codeStatus;
    private LocalDateTime fechaHora;

    public ErrorResponse(String mensaje, int codeStatus, LocalDateTime fechaHora) {
        this.mensaje = mensaje;
        this.codeStatus = codeStatus;
        this.fechaHora = fechaHora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(int codeStatus) {
        this.codeStatus = codeStatus;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
