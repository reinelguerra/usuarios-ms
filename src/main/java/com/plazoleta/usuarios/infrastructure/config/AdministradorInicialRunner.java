package com.plazoleta.usuarios.infrastructure.config;

import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import com.plazoleta.usuarios.infrastructure.output.jpa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdministradorInicialRunner implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final String correo;
    private final String clave;

    public AdministradorInicialRunner(UsuarioRepository usuarioRepository,
                                      PasswordEncoderPort passwordEncoderPort,
                                      @Value("${app.admin.correo:admin@plazoleta.com}") String correo,
                                      @Value("${app.admin.clave:Admin123*}") String clave) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.correo = correo;
        this.clave = clave;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.existsByCorreo(correo)) {
            return;
        }
        UsuarioEntity administrador = new UsuarioEntity();
        administrador.setNombre("Administrador");
        administrador.setApellido("Plazoleta");
        administrador.setDocumentoIdentidad("1000000000");
        administrador.setCelular("+573000000000");
        administrador.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        administrador.setCorreo(correo);
        administrador.setClave(passwordEncoderPort.encriptar(clave));
        administrador.setRol(Rol.ADMINISTRADOR);
        usuarioRepository.save(administrador);
    }
}