package com.plazoleta.usuarios.infrastructure.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenGeneratorAdapterTest {

    private static final String SECRETO = "clave-solo-para-pruebas-con-al-menos-32-caracteres";
    private static final String OTRO_SECRETO = "otra-clave-distinta-para-pruebas-de-32-caracteres";

    private SecretKey clave(String secreto) {
        return new SecretKeySpec(secreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    private JwtDecoder decodificador(String secreto) {
        return NimbusJwtDecoder.withSecretKey(clave(secreto)).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private String generarTokenDePrueba() {
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(clave(SECRETO)));
        JwtTokenGeneratorAdapter adaptador = new JwtTokenGeneratorAdapter(encoder, 60);
        Usuario usuario = new Usuario();
        usuario.setId(7L);
        usuario.setCorreo("ana@mail.com");
        usuario.setRol(Rol.PROPIETARIO);
        return adaptador.generarToken(usuario);
    }

    @Test
    void generarToken_incluyeIdRolYCorreoAcordados() {
        Jwt jwt = decodificador(SECRETO).decode(generarTokenDePrueba());

        assertEquals(7L, ((Number) jwt.getClaim("id")).longValue());
        assertEquals("PROPIETARIO", jwt.getClaimAsString("rol"));
        assertEquals("ana@mail.com", jwt.getClaimAsString("correo"));
    }

    @Test
    void generarToken_expiraEnUnaHora() {
        Jwt jwt = decodificador(SECRETO).decode(generarTokenDePrueba());

        assertTrue(jwt.getExpiresAt().isAfter(Instant.now().plus(59, ChronoUnit.MINUTES)));
        assertTrue(jwt.getExpiresAt().isBefore(Instant.now().plus(61, ChronoUnit.MINUTES)));
    }

    @Test
    void tokenFirmadoConOtraClave_noSeAcepta() {
               String token = generarTokenDePrueba();
        JwtDecoder otroDecodificador = decodificador(OTRO_SECRETO);

        assertThrows(JwtException.class, () -> otroDecodificador.decode(token));
    }
}