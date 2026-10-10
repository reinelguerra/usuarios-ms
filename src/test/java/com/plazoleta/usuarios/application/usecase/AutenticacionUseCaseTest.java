package com.plazoleta.usuarios.application.usecase;

import com.plazoleta.usuarios.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.domain.exception.ValidacionException;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.TokenGeneratorPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacionUseCaseTest {

    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenGeneratorPort tokenGeneratorPort;

    private AutenticacionUseCase autenticacionUseCase;

    @BeforeEach
    void setUp() {
        autenticacionUseCase = new AutenticacionUseCase(
                usuarioPersistencePort, passwordEncoderPort, tokenGeneratorPort);
    }

    private Usuario usuarioAlmacenado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("admin@plazoleta.com");
        usuario.setClave("hashBcrypt");
        usuario.setRol(Rol.ADMINISTRADOR);
        return usuario;
    }

    @Test
    void iniciarSesion_credencialesCorrectas_devuelveElToken() {
        Usuario usuario = usuarioAlmacenado();
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("admin@plazoleta.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coincide("Admin123*", "hashBcrypt")).thenReturn(true);
        when(tokenGeneratorPort.generarToken(usuario)).thenReturn("token.jwt.firmado");

        String token = autenticacionUseCase.iniciarSesion("admin@plazoleta.com", "Admin123*");

        assertEquals("token.jwt.firmado", token);
    }

    @Test
    void iniciarSesion_correoInexistente_lanzaCredencialesInvalidasYNoGeneraToken() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("nadie@mail.com"))
                .thenReturn(Optional.empty());

        CredencialesInvalidasException excepcion = assertThrows(CredencialesInvalidasException.class,
                () -> autenticacionUseCase.iniciarSesion("nadie@mail.com", "cualquiera"));

        assertEquals("Correo o clave incorrectos", excepcion.getMessage());
        verify(tokenGeneratorPort, never()).generarToken(any(Usuario.class));
    }

    @Test
    void iniciarSesion_claveIncorrecta_lanzaElMismoErrorYNoGeneraToken() {
        when(usuarioPersistencePort.obtenerUsuarioPorCorreo("admin@plazoleta.com"))
                .thenReturn(Optional.of(usuarioAlmacenado()));
        when(passwordEncoderPort.coincide("equivocada", "hashBcrypt")).thenReturn(false);

        CredencialesInvalidasException excepcion = assertThrows(CredencialesInvalidasException.class,
                () -> autenticacionUseCase.iniciarSesion("admin@plazoleta.com", "equivocada"));

        assertEquals("Correo o clave incorrectos", excepcion.getMessage());
        verify(tokenGeneratorPort, never()).generarToken(any(Usuario.class));
    }

    @Test
    void iniciarSesion_sinCorreo_lanzaValidacionYNoConsultaNada() {
        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> autenticacionUseCase.iniciarSesion(" ", "Admin123*"));

        assertEquals("El correo y la clave son obligatorios", excepcion.getMessage());
        verifyNoInteractions(usuarioPersistencePort, passwordEncoderPort, tokenGeneratorPort);
    }

    @Test
    void iniciarSesion_sinClave_lanzaValidacionYNoConsultaNada() {
        ValidacionException excepcion = assertThrows(ValidacionException.class,
                () -> autenticacionUseCase.iniciarSesion("admin@plazoleta.com", null));

        assertEquals("El correo y la clave son obligatorios", excepcion.getMessage());
        verifyNoInteractions(usuarioPersistencePort, passwordEncoderPort, tokenGeneratorPort);
    }
}