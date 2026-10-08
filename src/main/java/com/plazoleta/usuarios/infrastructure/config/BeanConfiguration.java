package com.plazoleta.usuarios.infrastructure.config;

import com.plazoleta.usuarios.application.usecase.UsuarioUseCase;
import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoleta.usuarios.infrastructure.output.jpa.adapter.UsuarioJpaAdapter;
import com.plazoleta.usuarios.infrastructure.output.jpa.mapper.UsuarioEntityMapper;
import com.plazoleta.usuarios.infrastructure.output.jpa.repository.UsuarioRepository;
import com.plazoleta.usuarios.infrastructure.security.BCryptPasswordEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UsuarioPersistencePort usuarioPersistencePort(UsuarioRepository usuarioRepository,
                                                         UsuarioEntityMapper usuarioEntityMapper) {
        return new UsuarioJpaAdapter(usuarioRepository, usuarioEntityMapper);
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        return new BCryptPasswordEncoderAdapter();
    }

    @Bean
    public UsuarioServicePort usuarioServicePort(UsuarioPersistencePort usuarioPersistencePort,
                                                 PasswordEncoderPort passwordEncoderPort) {
        return new UsuarioUseCase(usuarioPersistencePort, passwordEncoderPort);
    }
}