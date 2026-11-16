package com.plazoleta.usuarios.infrastructure.input.rest;

import com.plazoleta.usuarios.application.handler.AutenticacionHandler;
import com.plazoleta.usuarios.application.handler.UsuarioHandler;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.util.MensajeError;
import com.plazoleta.usuarios.infrastructure.exception.GlobalExceptionHandler;
import com.plazoleta.usuarios.infrastructure.input.rest.mapper.UsuarioRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Validación de los DTOs: responde 400 con el mensaje de siempre y no llega al handler. */
class UsuarioRestControllerValidacionTest {

    private static final String CLIENTES = "/api/v1/usuarios/clientes";
    private static final String PROPIETARIOS = "/api/v1/usuarios/propietarios";
    private static final String EMPLEADOS = "/api/v1/usuarios/empleados";
    private static final String LOGIN = "/api/v1/auth/login";

    private UsuarioHandler usuarioHandler;
    private MockMvc mockMvc;

    @BeforeEach
    void preparar() {
        usuarioHandler = mock(UsuarioHandler.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new UsuarioRestController(usuarioHandler, Mappers.getMapper(UsuarioRestMapper.class)),
                        new AuthRestController(mock(AutenticacionHandler.class)))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private static String texto(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }

    private static String cliente(String nombre, String documento, String celular, String correo, String clave) {
        return "{\"nombre\":" + texto(nombre) + ",\"apellido\":\"Lopez\",\"documentoIdentidad\":"
                + texto(documento) + ",\"celular\":" + texto(celular) + ",\"correo\":" + texto(correo)
                + ",\"clave\":" + texto(clave) + "}";
    }

    private ResultActions enviar(String url, String cuerpo) throws Exception {
        return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(cuerpo));
    }

    private void comprobarRechazo(ResultActions resultado, String mensaje) throws Exception {
        resultado.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(mensaje))
                .andExpect(jsonPath("$.status").value(400));
        verifyNoInteractions(usuarioHandler);
    }

    @Test
    void cliente_valido_llegaAlHandlerYDevuelve201() throws Exception {
        Usuario creado = new Usuario(1L, "Ana", "Lopez", "123456", "+573001234567", null,
                "ana@mail.com", "cifrada", Rol.CLIENTE, null);
        when(usuarioHandler.crearCliente(any(Usuario.class))).thenReturn(creado);

        enviar(CLIENTES, cliente("Ana", "123456", "+573001234567", "ana@mail.com", "secreta"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));

        verify(usuarioHandler).crearCliente(any(Usuario.class));
    }

    @Test
    void nombreVacio_devuelve400() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("  ", "123456", "+573001234567", "ana@mail.com", "x")),
                MensajeError.NOMBRE_OBLIGATORIO);
    }

    @Test
    void documentoConLetras_devuelve400() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("Ana", "12ab56", "+573001234567", "ana@mail.com", "x")),
                MensajeError.DOCUMENTO_INVALIDO);
    }

    @Test
    void celularDeMasDe13Caracteres_devuelve400() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("Ana", "123456", "+5730012345678", "ana@mail.com", "x")),
                MensajeError.CELULAR_INVALIDO);
    }

    @Test
    void correoSinArroba_devuelve400() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("Ana", "123456", "+573001234567", "ana.mail.com", "x")),
                MensajeError.CORREO_INVALIDO);
    }

    @Test
    void claveVacia_devuelve400() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("Ana", "123456", "+573001234567", "ana@mail.com", "")),
                MensajeError.CLAVE_OBLIGATORIA);
    }

    @Test
    void variosErrores_seListanTodosEnOrdenAlfabetico() throws Exception {
        comprobarRechazo(enviar(CLIENTES, cliente("", "123456", "+573001234567", "ana@mail.com", "")),
                MensajeError.NOMBRE_OBLIGATORIO + "; " + MensajeError.CLAVE_OBLIGATORIA);
    }

    @Test
    void propietarioSinFechaDeNacimiento_devuelve400() throws Exception {
        String cuerpo = "{\"nombre\":\"Pedro\",\"apellido\":\"Perez\",\"documentoIdentidad\":\"123456\","
                + "\"celular\":\"+573001234567\",\"correo\":\"pedro@mail.com\",\"clave\":\"secreta\"}";

        comprobarRechazo(enviar(PROPIETARIOS, cuerpo), MensajeError.FECHA_NACIMIENTO_OBLIGATORIA);
    }

    @Test
    void empleadoSinRestaurante_devuelve400() throws Exception {
        String cuerpo = cliente("Luis", "123456", "+573001234567", "luis@mail.com", "secreta");

        comprobarRechazo(enviar(EMPLEADOS, cuerpo), MensajeError.RESTAURANTE_EMPLEADO_INVALIDO);
    }

    @Test
    void loginSinClave_devuelve400() throws Exception {
        comprobarRechazo(enviar(LOGIN, "{\"correo\":\"ana@mail.com\"}"),
                MensajeError.CORREO_Y_CLAVE_OBLIGATORIOS);
    }
}