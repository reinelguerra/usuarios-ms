package com.plazoleta.usuarios.infrastructure.input.rest;

import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioBasicoRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioConsultaDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioResponseDto;
import com.plazoleta.usuarios.infrastructure.input.rest.mapper.UsuarioRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioRestController {

    private final UsuarioServicePort usuarioServicePort;
    private final UsuarioRestMapper usuarioRestMapper;

    public UsuarioRestController(UsuarioServicePort usuarioServicePort,
                                 UsuarioRestMapper usuarioRestMapper) {
        this.usuarioServicePort = usuarioServicePort;
        this.usuarioRestMapper = usuarioRestMapper;
    }

    @PostMapping("/propietarios")
    public ResponseEntity<UsuarioResponseDto> crearPropietario(@RequestBody UsuarioRequestDto request) {
        Usuario creado = usuarioServicePort.crearPropietario(usuarioRestMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(creado));
    }

    @PostMapping("/empleados")
    public ResponseEntity<UsuarioResponseDto> crearEmpleado(@RequestBody UsuarioBasicoRequestDto request) {
        Usuario creado = usuarioServicePort.crearEmpleado(usuarioRestMapper.toModelBasico(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioConsultaDto> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioServicePort.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuarioRestMapper.toConsulta(usuario));
    }
}