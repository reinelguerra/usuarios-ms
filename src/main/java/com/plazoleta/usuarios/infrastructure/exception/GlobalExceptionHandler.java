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
import com.plazoleta.usuarios.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.domain.util.MensajeError;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return construir(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

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
        return construir(HttpStatus.CONFLICT, MensajeError.USUARIO_DUPLICADO);    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoInvalido(HttpMessageNotReadableException ex) {
        return construir(HttpStatus.BAD_REQUEST, MensajeError.CUERPO_INVALIDO);    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return construir(HttpStatus.BAD_REQUEST, MensajeError.PARAMETRO_RUTA_INVALIDO);    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .sorted()
                .collect(Collectors.joining("; "));
        return construir(HttpStatus.BAD_REQUEST, mensaje);
    }
    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(new ErrorResponse(mensaje, status.value()));
    }
}