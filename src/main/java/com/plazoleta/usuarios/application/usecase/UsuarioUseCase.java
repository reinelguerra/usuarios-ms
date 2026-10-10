package com.plazoleta.usuarios.application.usecase;

import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios.domain.exception.ValidacionException;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UsuarioUseCase implements UsuarioServicePort {

    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern SOLO_DIGITOS = Pattern.compile("^\\d+$");
    private static final Pattern CELULAR = Pattern.compile("^\\+?\\d+$");
    private static final int CELULAR_MAX_CARACTERES = 13;
    private static final int EDAD_MINIMA = 18;

    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UsuarioUseCase(UsuarioPersistencePort usuarioPersistencePort,
                          PasswordEncoderPort passwordEncoderPort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Usuario crearPropietario(Usuario usuario) {
        validarDatosBasicos(usuario);
        validarMayorDeEdad(usuario);
        return registrar(usuario, Rol.PROPIETARIO);
    }

    @Override
    public Usuario crearEmpleado(Usuario usuario) {
        validarDatosBasicos(usuario);
        return registrar(usuario, Rol.EMPLEADO);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioPersistencePort.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    private Usuario registrar(Usuario usuario, Rol rol) {
        usuario.setRol(rol);
        usuario.setClave(passwordEncoderPort.encriptar(usuario.getClave()));
        return usuarioPersistencePort.guardarUsuario(usuario);
    }

    private void validarDatosBasicos(Usuario usuario) {
        if (usuario == null) {
            throw new ValidacionException("Los datos del usuario son obligatorios");
        }
        if (esVacio(usuario.getNombre())) {
            throw new ValidacionException("El nombre es obligatorio");
        }
        if (esVacio(usuario.getApellido())) {
            throw new ValidacionException("El apellido es obligatorio");
        }
        if (esVacio(usuario.getDocumentoIdentidad())
                || !SOLO_DIGITOS.matcher(usuario.getDocumentoIdentidad()).matches()) {
            throw new ValidacionException("El documento de identidad es obligatorio y debe ser solo numérico");
        }
        if (esVacio(usuario.getCelular())
                || usuario.getCelular().length() > CELULAR_MAX_CARACTERES
                || !CELULAR.matcher(usuario.getCelular()).matches()) {
            throw new ValidacionException("El celular es obligatorio, máximo 13 caracteres y puede iniciar con +");
        }
        if (esVacio(usuario.getCorreo()) || !CORREO.matcher(usuario.getCorreo()).matches()) {
            throw new ValidacionException("El correo es obligatorio y debe tener una estructura válida");
        }
        if (esVacio(usuario.getClave())) {
            throw new ValidacionException("La clave es obligatoria");
        }
    }

    private void validarMayorDeEdad(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) {
            throw new ValidacionException("La fecha de nacimiento es obligatoria");
        }
        if (Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears() < EDAD_MINIMA) {
            throw new ValidacionException("El usuario debe ser mayor de edad");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}