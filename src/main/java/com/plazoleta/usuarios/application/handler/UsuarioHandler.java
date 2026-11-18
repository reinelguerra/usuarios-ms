package com.plazoleta.usuarios.application.handler;

import com.plazoleta.usuarios.domain.model.Usuario;

/** Puerto de entrada de la capa de aplicación para usuarios: lo único que conoce el controller. */
public interface UsuarioHandler {

    Usuario crearPropietario(Usuario usuario);

    Usuario crearEmpleado(Usuario usuario);

    Usuario crearCliente(Usuario usuario);

    Usuario obtenerUsuarioPorId(Long id);
}