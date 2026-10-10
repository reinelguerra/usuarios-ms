package com.plazoleta.usuarios.domain.spi;

public interface PasswordEncoderPort {

    String encriptar(String claveSinEncriptar);

    boolean coincide(String claveSinEncriptar, String claveEncriptada);
}