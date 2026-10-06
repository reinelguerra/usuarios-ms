package com.plazoleta.usuarios.domain.exception;

public class ValidacionException extends RuntimeException {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}