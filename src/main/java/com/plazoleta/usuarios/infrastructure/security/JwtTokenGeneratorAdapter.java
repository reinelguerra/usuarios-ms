package com.plazoleta.usuarios.infrastructure.security;

import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.TokenGeneratorPort;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JwtTokenGeneratorAdapter implements TokenGeneratorPort {

    private final JwtEncoder jwtEncoder;
    private final long minutosDeVigencia;

    public JwtTokenGeneratorAdapter(JwtEncoder jwtEncoder, long minutosDeVigencia) {
        this.jwtEncoder = jwtEncoder;
        this.minutosDeVigencia = minutosDeVigencia;
    }

    @Override
    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(usuario.getCorreo())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(minutosDeVigencia, ChronoUnit.MINUTES))
                .claim("id", usuario.getId())
                .claim("correo", usuario.getCorreo())
                .claim("rol", usuario.getRol().name())
                .build();
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(cabecera, claims)).getTokenValue();
    }
}