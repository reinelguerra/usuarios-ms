package com.plazoleta.usuarios.infrastructure.input.rest.dto;

import com.plazoleta.usuarios.domain.util.MensajeError;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = MensajeError.CORREO_Y_CLAVE_OBLIGATORIOS)
        String correo,

        @NotBlank(message = MensajeError.CORREO_Y_CLAVE_OBLIGATORIOS)
        String clave) {
}