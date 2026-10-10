package com.plazoleta.usuarios.domain.api;

public interface AutenticacionServicePort {

    String iniciarSesion(String correo, String clave);
}