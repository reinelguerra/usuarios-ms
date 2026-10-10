package com.plazoleta.usuarios.domain.spi;

import com.plazoleta.usuarios.domain.model.Usuario;

public interface TokenGeneratorPort {

    String generarToken(Usuario usuario);
}