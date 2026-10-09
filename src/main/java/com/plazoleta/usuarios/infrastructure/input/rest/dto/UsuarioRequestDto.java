package com.plazoleta.usuarios.infrastructure.input.rest.dto;

import java.time.LocalDate;

public record UsuarioRequestDto(
        String nombre,
        String apellido,
        String documentoIdentidad,
        String celular,
        LocalDate fechaNacimiento,
        String correo,
        String clave) {
}