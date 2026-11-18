package com.plazoleta.usuarios.domain.util;

public final class MensajeError {

    public static final String CORREO_Y_CLAVE_OBLIGATORIOS = "El correo y la clave son obligatorios";

    public static final String USUARIO_OBLIGATORIO = "Los datos del usuario son obligatorios";
    public static final String NOMBRE_OBLIGATORIO = "El nombre es obligatorio";
    public static final String APELLIDO_OBLIGATORIO = "El apellido es obligatorio";
    public static final String DOCUMENTO_INVALIDO =
            "El documento de identidad es obligatorio y debe ser solo numérico";
    public static final String CELULAR_INVALIDO =
            "El celular es obligatorio, máximo 13 caracteres y puede iniciar con +";
    public static final String CORREO_INVALIDO =
            "El correo es obligatorio y debe tener una estructura válida";
    public static final String CLAVE_OBLIGATORIA = "La clave es obligatoria";
    public static final String RESTAURANTE_EMPLEADO_INVALIDO =
            "El restaurante del empleado es obligatorio y debe ser un id válido";
    public static final String FECHA_NACIMIENTO_OBLIGATORIA = "La fecha de nacimiento es obligatoria";
    public static final String DEBE_SER_MAYOR_DE_EDAD = "El usuario debe ser mayor de edad";

    public static final String USUARIO_DUPLICADO =
            "El correo o el documento de identidad ya están registrados";
    public static final String CUERPO_INVALIDO =
            "El cuerpo de la petición no es válido. Revisa los campos y que la fecha tenga el formato yyyy-MM-dd";
    public static final String PARAMETRO_RUTA_INVALIDO = "El valor del parámetro en la ruta no es válido";

    private MensajeError() {
    }
}   