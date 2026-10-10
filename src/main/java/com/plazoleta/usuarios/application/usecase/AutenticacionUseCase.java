package com.plazoleta.usuarios.application.usecase;

import com.plazoleta.usuarios.domain.api.AutenticacionServicePort;
import com.plazoleta.usuarios.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios.domain.exception.ValidacionException;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.TokenGeneratorPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;

public class AutenticacionUseCase implements AutenticacionServicePort {

    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenGeneratorPort tokenGeneratorPort;

    public AutenticacionUseCase(UsuarioPersistencePort usuarioPersistencePort,
                                PasswordEncoderPort passwordEncoderPort,
                                TokenGeneratorPort tokenGeneratorPort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override
    public String iniciarSesion(String correo, String clave) {
        if (esVacio(correo) || esVacio(clave)) {
            throw new ValidacionException("El correo y la clave son obligatorios");
        }
        Usuario usuario = usuarioPersistencePort.obtenerUsuarioPorCorreo(correo)
                .orElseThrow(CredencialesInvalidasException::new);
        if (!passwordEncoderPort.coincide(clave, usuario.getClave())) {
            throw new CredencialesInvalidasException();
        }
        return tokenGeneratorPort.generarToken(usuario);
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}