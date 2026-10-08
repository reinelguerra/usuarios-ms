package com.plazoleta.usuarios.domain.spi;

import com.plazoleta.usuarios.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistencePort {

    Usuario guardarUsuario(Usuario usuario);

    Optional<Usuario> obtenerUsuarioPorId(Long id);
}