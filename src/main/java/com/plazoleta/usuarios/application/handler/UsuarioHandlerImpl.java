package com.plazoleta.usuarios.application.handler;

import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.model.Usuario;

public class UsuarioHandlerImpl implements UsuarioHandler {

    private final UsuarioServicePort usuarioServicePort;

    public UsuarioHandlerImpl(UsuarioServicePort usuarioServicePort) {
        this.usuarioServicePort = usuarioServicePort;
    }

    @Override
    public Usuario crearPropietario(Usuario usuario) {
        return usuarioServicePort.crearPropietario(usuario);
    }

    @Override
    public Usuario crearEmpleado(Usuario usuario) {
        return usuarioServicePort.crearEmpleado(usuario);
    }

    @Override
    public Usuario crearCliente(Usuario usuario) {
        return usuarioServicePort.crearCliente(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioServicePort.obtenerUsuarioPorId(id);
    }
}