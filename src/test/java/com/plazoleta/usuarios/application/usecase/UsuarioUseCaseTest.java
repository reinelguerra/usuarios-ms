package com.plazoleta.usuarios.application.usecase;

import com.plazoleta.usuarios.domain.exception.ValidacionException;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    private UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        usuarioUseCase = new UsuarioUseCase(usuarioPersistencePort, passwordEncoderPort);
    }

    private Usuario usuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setApellido("Perez");
        usuario.setDocumentoIdentidad("123456789");
        usuario.setCelular("+573005698325");
        usuario.setFechaNacimiento(LocalDate.of(1990, 5, 20));
        usuario.setCorreo("ana.perez@mail.com");
        usuario.setClave("secreta");
        return usuario;
    }

    private void assertValidacion(String mensajeEsperado, Usuario usuario) {
        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearPropietario(usuario));

        assertEquals(mensajeEsperado, excepcion.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
        verifyNoInteractions(passwordEncoderPort);
    }

    @Test
    void crearPropietario_conDatosValidos_guardaConRolPropietarioYClaveEncriptada() {
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearPropietario(usuarioValido());

        assertEquals(Rol.PROPIETARIO, resultado.getRol());
        assertEquals("claveEncriptada", resultado.getClave());
        verify(usuarioPersistencePort).guardarUsuario(resultado);
    }

    @Test
    void crearPropietario_conExactamente18Anios_loPermite() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(LocalDate.now().minusYears(18));
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearPropietario(usuario);

        assertEquals(Rol.PROPIETARIO, resultado.getRol());
    }

    @Test
    void crearPropietario_usuarioNulo_lanzaValidacion() {
        assertValidacion("Los datos del usuario son obligatorios", null);
    }

    @Test
    void crearPropietario_sinNombre_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setNombre(" ");
        assertValidacion("El nombre es obligatorio", usuario);
    }

    @Test
    void crearPropietario_sinApellido_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setApellido(null);
        assertValidacion("El apellido es obligatorio", usuario);
    }

    @Test
    void crearPropietario_documentoConLetras_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setDocumentoIdentidad("12a45");
        assertValidacion("El documento de identidad es obligatorio y debe ser solo numérico", usuario);
    }

    @Test
    void crearPropietario_celularConMasDe13Caracteres_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setCelular("+5730056983251");
        assertValidacion("El celular es obligatorio, máximo 13 caracteres y puede iniciar con +", usuario);
    }

    @Test
    void crearPropietario_celularConLetras_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setCelular("300abc");
        assertValidacion("El celular es obligatorio, máximo 13 caracteres y puede iniciar con +", usuario);
    }

    @Test
    void crearPropietario_sinFechaDeNacimiento_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);
        assertValidacion("La fecha de nacimiento es obligatoria", usuario);
    }

    @Test
    void crearPropietario_menorDeEdad_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(LocalDate.now().minusYears(17));
        assertValidacion("El usuario debe ser mayor de edad", usuario);
    }

    @Test
    void crearPropietario_correoSinArroba_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setCorreo("anamail.com");
        assertValidacion("El correo es obligatorio y debe tener una estructura válida", usuario);
    }

    @Test
    void crearPropietario_sinClave_lanzaValidacion() {
        Usuario usuario = usuarioValido();
        usuario.setClave(null);
        assertValidacion("La clave es obligatoria", usuario);
    }

        @Test
    void crearEmpleado_conDatosValidosSinFecha_guardaConRolEmpleadoYClaveEncriptada() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearEmpleado(usuario);

        assertEquals(Rol.EMPLEADO, resultado.getRol());
        assertEquals("claveEncriptada", resultado.getClave());
        verify(usuarioPersistencePort).guardarUsuario(resultado);
    }

    @Test
    void crearEmpleado_ignoraElRolQueVengaEnLosDatos() {
        Usuario usuario = usuarioValido();
        usuario.setRol(Rol.ADMINISTRADOR);
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearEmpleado(usuario);

        assertEquals(Rol.EMPLEADO, resultado.getRol());
    }

    @Test
    void crearEmpleado_documentoConLetras_lanzaValidacionYNoGuarda() {
        Usuario usuario = usuarioValido();
        usuario.setDocumentoIdentidad("12a45");

        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearEmpleado(usuario));

        assertEquals("El documento de identidad es obligatorio y debe ser solo numérico", excepcion.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
        verifyNoInteractions(passwordEncoderPort);
    }

    @Test
    void crearEmpleado_sinCorreo_lanzaValidacionYNoGuarda() {
        Usuario usuario = usuarioValido();
        usuario.setCorreo(null);

        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearEmpleado(usuario));

        assertEquals("El correo es obligatorio y debe tener una estructura válida", excepcion.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void crearEmpleado_usuarioNulo_lanzaValidacion() {
        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearEmpleado(null));

        assertEquals("Los datos del usuario son obligatorios", excepcion.getMessage());
    }
        @Test
    void crearCliente_conDatosValidosSinFecha_guardaConRolClienteYClaveEncriptada() {
        Usuario usuario = usuarioValido();
        usuario.setFechaNacimiento(null);
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearCliente(usuario);

        assertEquals(Rol.CLIENTE, resultado.getRol());
        assertEquals("claveEncriptada", resultado.getClave());
        verify(usuarioPersistencePort).guardarUsuario(resultado);
    }

    @Test
    void crearCliente_ignoraElRolQueVengaEnLosDatos() {
        Usuario usuario = usuarioValido();
        usuario.setRol(Rol.ADMINISTRADOR);
        when(passwordEncoderPort.encriptar("secreta")).thenReturn("claveEncriptada");
        when(usuarioPersistencePort.guardarUsuario(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioUseCase.crearCliente(usuario);

        assertEquals(Rol.CLIENTE, resultado.getRol());
    }

    @Test
    void crearCliente_celularInvalido_lanzaValidacionYNoGuarda() {
        Usuario usuario = usuarioValido();
        usuario.setCelular("300abc");

        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearCliente(usuario));

        assertEquals("El celular es obligatorio, máximo 13 caracteres y puede iniciar con +", excepcion.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
        verifyNoInteractions(passwordEncoderPort);
    }

    @Test
    void crearCliente_sinClave_lanzaValidacionYNoGuarda() {
        Usuario usuario = usuarioValido();
        usuario.setClave(null);

        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> usuarioUseCase.crearCliente(usuario));

        assertEquals("La clave es obligatoria", excepcion.getMessage());
        verify(usuarioPersistencePort, never()).guardarUsuario(any(Usuario.class));
    }
}