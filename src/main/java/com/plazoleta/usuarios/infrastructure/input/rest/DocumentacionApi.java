package com.plazoleta.usuarios.infrastructure.input.rest;


public final class DocumentacionApi {

    public static final String NO_AUTENTICADO = "No autenticado: falta el token o no es válido";
    public static final String SIN_PERMISO = "Sin permiso: el rol del token no puede hacer esta acción";
    public static final String DATOS_INVALIDOS = "Datos inválidos o regla de negocio incumplida";
    public static final String DUPLICADO = "El correo o el documento de identidad ya están registrados";
    public static final String USUARIO_NO_ENCONTRADO = "No existe un usuario con ese id";
    public static final String CREDENCIALES_INVALIDAS = "Correo o clave incorrectos";

    private DocumentacionApi() {
        
    }
}