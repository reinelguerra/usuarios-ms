package com.plazoleta.usuarios.infrastructure.input.rest.dto;

import com.plazoleta.usuarios.domain.model.Rol;

public record UsuarioConsultaDto(
        Long id,
        String nombre,
        String apellido,
        String correo,
        Rol rol) {
}