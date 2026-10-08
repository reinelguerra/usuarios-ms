package com.plazoleta.usuarios.infrastructure.input.rest.mapper;

import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioConsultaDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    Usuario toModel(UsuarioRequestDto request);

    UsuarioResponseDto toResponse(Usuario usuario);

    UsuarioConsultaDto toConsulta(Usuario usuario);
}