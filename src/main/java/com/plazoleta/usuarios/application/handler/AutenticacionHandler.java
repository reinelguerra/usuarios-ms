package com.plazoleta.usuarios.application.handler;

/** Puerto de entrada de la capa de aplicación para la autenticación. */
public interface AutenticacionHandler {

    String iniciarSesion(String correo, String clave);
}