package com.plazoleta.usuarios.infrastructure.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.plazoleta.usuarios.application.usecase.AutenticacionUseCase;
import com.plazoleta.usuarios.domain.api.AutenticacionServicePort;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.TokenGeneratorPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoleta.usuarios.infrastructure.security.JwtTokenGeneratorAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class AutenticacionBeanConfiguration {

    @Bean
    public SecretKey jwtSecretKey(@Value("${jwt.secret}") String secreto) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public TokenGeneratorPort tokenGeneratorPort(JwtEncoder jwtEncoder,
                                                 @Value("${jwt.minutos-vigencia:60}") long minutosDeVigencia) {
        return new JwtTokenGeneratorAdapter(jwtEncoder, minutosDeVigencia);
    }

    @Bean
    public AutenticacionServicePort autenticacionServicePort(UsuarioPersistencePort usuarioPersistencePort,
                                                             PasswordEncoderPort passwordEncoderPort,
                                                             TokenGeneratorPort tokenGeneratorPort) {
        return new AutenticacionUseCase(usuarioPersistencePort, passwordEncoderPort, tokenGeneratorPort);
    }
}