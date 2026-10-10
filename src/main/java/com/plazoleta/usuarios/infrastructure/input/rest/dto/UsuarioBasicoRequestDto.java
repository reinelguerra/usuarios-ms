package com.plazoleta.usuarios.infrastructure.input.rest.dto;

public record UsuarioBasicoRequestDto(
        String nombre,
        String apellido,
        String documentoIdentidad,
        String celular,
        String correo,
        String clave) {
}