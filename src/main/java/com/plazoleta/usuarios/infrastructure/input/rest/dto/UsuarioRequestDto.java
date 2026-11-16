package com.plazoleta.usuarios.infrastructure.input.rest.dto;

import com.plazoleta.usuarios.domain.util.MensajeError;
import com.plazoleta.usuarios.domain.util.ReglasUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioRequestDto(
        @NotBlank(message = MensajeError.NOMBRE_OBLIGATORIO)
        String nombre,

        @NotBlank(message = MensajeError.APELLIDO_OBLIGATORIO)
        String apellido,

        @NotBlank(message = MensajeError.DOCUMENTO_INVALIDO)
        @Pattern(regexp = ReglasUsuario.REGEX_SOLO_DIGITOS, message = MensajeError.DOCUMENTO_INVALIDO)
        String documentoIdentidad,

        @NotBlank(message = MensajeError.CELULAR_INVALIDO)
        @Size(max = ReglasUsuario.CELULAR_MAX_CARACTERES, message = MensajeError.CELULAR_INVALIDO)
        @Pattern(regexp = ReglasUsuario.REGEX_CELULAR, message = MensajeError.CELULAR_INVALIDO)
        String celular,

        @NotNull(message = MensajeError.FECHA_NACIMIENTO_OBLIGATORIA)
        LocalDate fechaNacimiento,

        @NotBlank(message = MensajeError.CORREO_INVALIDO)
        @Size(max = ReglasUsuario.CORREO_MAX_CARACTERES, message = MensajeError.CORREO_INVALIDO)
        @Pattern(regexp = ReglasUsuario.REGEX_CORREO, message = MensajeError.CORREO_INVALIDO)
        String correo,

        @NotBlank(message = MensajeError.CLAVE_OBLIGATORIA)
        String clave) {
}