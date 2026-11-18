package com.plazoleta.usuarios.application.handler;

import com.plazoleta.usuarios.domain.api.AutenticacionServicePort;

public class AutenticacionHandlerImpl implements AutenticacionHandler {

    private final AutenticacionServicePort autenticacionServicePort;

    public AutenticacionHandlerImpl(AutenticacionServicePort autenticacionServicePort) {
        this.autenticacionServicePort = autenticacionServicePort;
    }

    @Override
    public String iniciarSesion(String correo, String clave) {
        return autenticacionServicePort.iniciarSesion(correo, clave);
    }
}
