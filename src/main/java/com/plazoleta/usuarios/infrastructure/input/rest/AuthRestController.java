package com.plazoleta.usuarios.infrastructure.input.rest;

import com.plazoleta.usuarios.domain.api.AutenticacionServicePort;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.LoginRequestDto;
import com.plazoleta.usuarios.infrastructure.input.rest.dto.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AutenticacionServicePort autenticacionServicePort;

    public AuthRestController(AutenticacionServicePort autenticacionServicePort) {
        this.autenticacionServicePort = autenticacionServicePort;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = autenticacionServicePort.iniciarSesion(request.correo(), request.clave());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}