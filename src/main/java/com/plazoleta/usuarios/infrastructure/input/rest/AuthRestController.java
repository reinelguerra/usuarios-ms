package com.plazoleta.usuarios.infrastructure.input.rest;

import com.plazoleta.usuarios.domain.api.AutenticacionServicePort;
import com.plazoleta.usuarios.infrastructure.exception.ErrorResponse;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.LoginRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.CREDENCIALES_INVALIDAS;
import static com.plazoleta.usuarios.infrastructure.input.rest.DocumentacionApi.DATOS_INVALIDOS;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Inicio de sesión y generación del token JWT")
public class AuthRestController {

    private final AutenticacionServicePort autenticacionServicePort;

    public AuthRestController(AutenticacionServicePort autenticacionServicePort) {
        this.autenticacionServicePort = autenticacionServicePort;
    }

    @Operation(summary = "Iniciar sesión",
            description = "Público: no requiere token. Con el correo y la clave devuelve un token JWT de "
                    + "vigencia limitada, que lleva el id, el correo y el rol del usuario. "
                    + "Se envía en las demás peticiones como Authorization: Bearer <token>.")
    @SecurityRequirements
    @ApiResponse(responseCode = "400", description = DATOS_INVALIDOS,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = CREDENCIALES_INVALIDAS,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto: devuelve el token JWT",
            content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = autenticacionServicePort.iniciarSesion(request.correo(), request.clave());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}