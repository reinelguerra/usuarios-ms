package com.plazoleta.usuarios.infrastructure.output.jpa.repository;

import com.plazoleta.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByCorreo(String correo);

    Optional<UsuarioEntity> findByCorreo(String correo);
}