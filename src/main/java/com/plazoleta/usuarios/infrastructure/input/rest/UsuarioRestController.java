package com.plazoleta.usuarios.infrastructure.input.rest;

import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.infrastructure.exception.ErrorResponse;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioBasicoRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioConsultaDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.UsuarioResponseDto;
import com.plazoleta.usuarios.infrastructure.input.rest.mapper.UsuarioRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.DATOS_INVALIDOS;
import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.DUPLICADO;
import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.NO_AUTENTICADO;
import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.SIN_PERMISO;
import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.USUARIO_NO_ENCONTRADO;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Creación de cuentas por rol y consulta de usuarios")
@ApiResponse(responseCode = "400", description = DATOS_INVALIDOS,
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class UsuarioRestController {

    private final UsuarioServicePort usuarioServicePort;
    private final UsuarioRestMapper usuarioRestMapper;

    public UsuarioRestController(UsuarioServicePort usuarioServicePort,
                                 UsuarioRestMapper usuarioRestMapper) {
        this.usuarioServicePort = usuarioServicePort;
        this.usuarioRestMapper = usuarioRestMapper;
    }

    @Operation(summary = "Crear un propietario",
            description = "Solo el ADMINISTRADOR. Crea un usuario con rol PROPIETARIO. La clave se guarda "
                    + "encriptada y la fecha de nacimiento va en formato yyyy-MM-dd. Si un dato no cumple "
                    + "las reglas de validación, responde 400 con el motivo.")
    @ApiResponse(responseCode = "401", description = NO_AUTENTICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = SIN_PERMISO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = DUPLICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/propietarios")
    public ResponseEntity<UsuarioResponseDto> crearPropietario(@RequestBody UsuarioRequestDto request) {
        Usuario creado = usuarioServicePort.crearPropietario(usuarioRestMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(creado));
    }

    @Operation(summary = "Crear un empleado",
            description = "Solo el PROPIETARIO. Crea un usuario con rol EMPLEADO. La clave se guarda "
                    + "encriptada. Si un dato no cumple las reglas de validación, responde 400 con el motivo.")
    @ApiResponse(responseCode = "401", description = NO_AUTENTICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = SIN_PERMISO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = DUPLICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/empleados")
    public ResponseEntity<UsuarioResponseDto> crearEmpleado(@RequestBody UsuarioBasicoRequestDto request) {
        Usuario creado = usuarioServicePort.crearEmpleado(usuarioRestMapper.toModelBasico(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(creado));
    }

    @Operation(summary = "Registrar un cliente",
            description = "Público: no requiere token. Crea un usuario con rol CLIENTE. La clave se guarda "
                    + "encriptada. Si un dato no cumple las reglas de validación, responde 400 con el motivo.")
    @SecurityRequirements
    @ApiResponse(responseCode = "409", description = DUPLICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/clientes")
    public ResponseEntity<UsuarioResponseDto> crearCliente(@RequestBody UsuarioBasicoRequestDto request) {
        Usuario creado = usuarioServicePort.crearCliente(usuarioRestMapper.toModelBasico(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRestMapper.toResponse(creado));
    }

    @Operation(summary = "Consultar un usuario por id",
            description = "Cualquier usuario autenticado, sin importar su rol. Devuelve el id, nombre, "
                    + "apellido, correo y rol (nunca la clave). Lo usa plazoleta-ms para comprobar que "
                    + "el propietario de un restaurante existe y tiene rol PROPIETARIO.")
    @ApiResponse(responseCode = "401", description = NO_AUTENTICADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = USUARIO_NO_ENCONTRADO,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = UsuarioConsultaDto.class)))
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioConsultaDto> obtenerUsuario(
            @Parameter(description = "Identificador del usuario", example = "1")
            @PathVariable Long id) {
        Usuario usuario = usuarioServicePort.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuarioRestMapper.toConsulta(usuario));
    }
}