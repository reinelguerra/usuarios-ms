package com.plazoleta.usuarios.infrastructure.input.rest.dto;

import com.plazoleta.usuarios.domain.model.Rol;

import java.time.LocalDate;

public record UsuarioResponseDto(
        Long id,
        String nombre,
        String apellido,
        String documentoIdentidad,
        String celular,
        LocalDate fechaNacimiento,
        String correo,
        Rol rol) {
}