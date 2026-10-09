package com.plazoleta.usuarios.infrastructure.output.jpa.adapter;

import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoleta.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import com.plazoleta.usuarios.infrastructure.output.jpa.mapper.UsuarioEntityMapper;
import com.plazoleta.usuarios.infrastructure.output.jpa.repository.UsuarioRepository;
import java.util.Optional;


public class UsuarioJpaAdapter implements UsuarioPersistencePort {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioJpaAdapter(UsuarioRepository usuarioRepository,
                             UsuarioEntityMapper usuarioEntityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        UsuarioEntity guardado = usuarioRepository.save(usuarioEntityMapper.toEntity(usuario));
        return usuarioEntityMapper.toModel(guardado);
    }
        @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).map(usuarioEntityMapper::toModel);
    }
}