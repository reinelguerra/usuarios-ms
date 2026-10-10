package com.plazoleta.usuarios.infrastructure.security;

import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encriptar(String claveSinEncriptar) {
        return encoder.encode(claveSinEncriptar);
    }

    @Override
    public boolean coincide(String claveSinEncriptar, String claveEncriptada) {
        return encoder.matches(claveSinEncriptar, claveEncriptada);
    }
}