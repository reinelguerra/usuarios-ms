package com.plazoleta.usuarios.application.usecase;

import com.plazoleta.usuarios.domain.api.UsuarioServicePort;
import com.plazoleta.usuarios.domain.exception.UsuarioNoEncontradoException;
import com.plazoleta.usuarios.domain.exception.ValidacionException;
import com.plazoleta.usuarios.domain.model.Rol;
import com.plazoleta.usuarios.domain.model.Usuario;
import com.plazoleta.usuarios.domain.spi.PasswordEncoderPort;
import com.plazoleta.usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoleta.usuarios.domain.util.MensajeError;
import com.plazoleta.usuarios.domain.util.ReglasUsuario;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class UsuarioUseCase implements UsuarioServicePort {

     private static final Pattern CORREO = Pattern.compile(ReglasUsuario.REGEX_CORREO);
    private static final int CORREO_MAX_CARACTERES = ReglasUsuario.CORREO_MAX_CARACTERES;
    private static final Pattern SOLO_DIGITOS = Pattern.compile(ReglasUsuario.REGEX_SOLO_DIGITOS);
    private static final Pattern CELULAR = Pattern.compile(ReglasUsuario.REGEX_CELULAR);
    private static final int CELULAR_MAX_CARACTERES = ReglasUsuario.CELULAR_MAX_CARACTERES;
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
        validarRestauranteDelEmpleado(usuario);
        return registrar(usuario, Rol.EMPLEADO);
    }

    @Override
    public Usuario crearCliente(Usuario usuario) {
        validarDatosBasicos(usuario);
        return registrar(usuario, Rol.CLIENTE);
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
            throw new ValidacionException(MensajeError.USUARIO_OBLIGATORIO);
        }
        if (esVacio(usuario.getNombre())) {
            throw new ValidacionException(MensajeError.NOMBRE_OBLIGATORIO);
        }
        if (esVacio(usuario.getApellido())) {
            throw new ValidacionException(MensajeError.APELLIDO_OBLIGATORIO);
        }
        if (esVacio(usuario.getDocumentoIdentidad())
                || !SOLO_DIGITOS.matcher(usuario.getDocumentoIdentidad()).matches()) {
            throw new ValidacionException(MensajeError.DOCUMENTO_INVALIDO);
        }
        if (esVacio(usuario.getCelular())
                || usuario.getCelular().length() > CELULAR_MAX_CARACTERES
                || !CELULAR.matcher(usuario.getCelular()).matches()) {
            throw new ValidacionException(MensajeError.CELULAR_INVALIDO);
        }
        if (esVacio(usuario.getCorreo())
                || usuario.getCorreo().length() > CORREO_MAX_CARACTERES
                || !CORREO.matcher(usuario.getCorreo()).matches()) {
            throw new ValidacionException(MensajeError.CORREO_INVALIDO);
        }
        if (esVacio(usuario.getClave())) {
            throw new ValidacionException(MensajeError.CLAVE_OBLIGATORIA);
        }
    }

    private void validarRestauranteDelEmpleado(Usuario usuario) {
        if (usuario.getIdRestaurante() == null || usuario.getIdRestaurante() <= 0) {
            throw new ValidacionException(MensajeError.RESTAURANTE_EMPLEADO_INVALIDO);
        }
    }

    private void validarMayorDeEdad(Usuario usuario) {
        if (usuario.getFechaNacimiento() == null) {
            throw new ValidacionException(MensajeError.FECHA_NACIMIENTO_OBLIGATORIA);
        }
        if (Period.between(usuario.getFechaNacimiento(), LocalDate.now(ZoneId.systemDefault()))
                .getYears() < EDAD_MINIMA) {
            throw new ValidacionException(MensajeError.DEBE_SER_MAYOR_DE_EDAD);
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}