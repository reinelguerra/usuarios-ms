package com.plazoleta.usuarios.infrastructure.exception;

import com.plazoleta.usuarios.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios.domain.exception.ValidacionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(ValidacionException ex) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(UsuarioNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(DataIntegrityViolationException ex) {
        return construir(HttpStatus.CONFLICT, "El correo o el documento de identidad ya están registrados");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoInvalido(HttpMessageNotReadableException ex) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es válido. Revisa los campos y que la fecha tenga el formato yyyy-MM-dd");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return construir(HttpStatus.BAD_REQUEST, "El valor del parámetro en la ruta no es válido");
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(new ErrorResponse(mensaje, status.value()));
    }
}