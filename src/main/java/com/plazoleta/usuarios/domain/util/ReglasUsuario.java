package com.plazoleta.usuarios.domain.util;

/** Reglas de formato de un usuario: una sola fuente para el caso de uso y para los DTOs. */
public final class ReglasUsuario {

    public static final String REGEX_SOLO_DIGITOS = "^\\d+$";
    public static final String REGEX_CELULAR = "^\\+?\\d+$";
    public static final String REGEX_CORREO = "^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$";
    public static final int CELULAR_MAX_CARACTERES = 13;
    public static final int CORREO_MAX_CARACTERES = 254;

    private ReglasUsuario() {
    }
}