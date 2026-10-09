package com.plazoleta.usuarios.infrastructure.output.jpa.mapper;

import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {

    UsuarioEntity toEntity(Usuario usuario);

    Usuario toModel(UsuarioEntity entity);
}