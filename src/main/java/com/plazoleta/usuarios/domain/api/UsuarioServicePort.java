package com.plazoleta.usuarios.domain.api;

import com.plazoleta.usuarios.domain.model.Usuario;

public interface UsuarioServicePort {

    Usuario crearPropietario(Usuario usuario);

    Usuario obtenerUsuarioPorId(Long id);
}