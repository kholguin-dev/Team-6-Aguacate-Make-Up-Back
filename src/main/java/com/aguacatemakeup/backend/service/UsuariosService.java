package com.aguacatemakeup.backend.service;

/*Entidades*/
import com.aguacatemakeup.backend.entity.Usuarios;
import com.aguacatemakeup.backend.entity.Rol;
/*DTO*/
import com.aguacatemakeup.backend.dto.RegistroUsuarioDTO;
import com.aguacatemakeup.backend.dto.UsuarioRespuestaDTO;
/*Repositorios*/
import com.aguacatemakeup.backend.repository.UsuariosRepository;
import com.aguacatemakeup.backend.repository.RolRepository;

/*Spring Security*/
import org.springframework.security.crypto.password.PasswordEncoder;

/*Lombok y Spring*/
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*Paginacion y colecciones*/
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    /*Metodo obtener usuarios con paginacion*/
    public Page<Usuarios> obtenerTodosLosUsuarios(Pageable pageable) {
        return usuariosRepository.findAll(pageable);
    }
    /*Obtener user por id*/
    public Optional<Usuarios> obtenerUsuarioPorId(Long idUsuario) {
        return usuariosRepository.findById(idUsuario);
    }
    /*Bucar por mail*/
    public Optional<Usuarios> buscarUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo);
    }

    /* Registrar usuario con contraseña Protegida*/
    public UsuarioRespuestaDTO registrarUsuario(RegistroUsuarioDTO datos) {
        /*Valida todos los campos obligatorios*/

        if (datos == null
                || datos.getNombres() == null || datos.getNombres().isBlank()
                || datos.getApellidos() == null || datos.getApellidos().isBlank()
                || datos.getCorreo() == null || datos.getCorreo().isBlank()
                || datos.getTelefono() == null || datos.getTelefono().isBlank()
                || datos.getContrasena() == null || datos.getContrasena().isBlank()) {

            throw new IllegalArgumentException(
                    "Todos los datos personales y la contraseña son obligatorios."
            );
        }

        /*Validar que acepte terminos*/
        if (!Boolean.TRUE.equals(datos.getAceptaTerminos())) {

            throw new IllegalArgumentException(
                    "Debes aceptar los términos y condiciones para registrarte."
            );
        }
        /*Validar que el correo no este registrado*/
        if (usuariosRepository.findByCorreo(datos.getCorreo().trim()).isPresent()) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado."
            );
        }
        /*Validar que el telefono no este registrado*/
        if (usuariosRepository.existsByTelefono(datos.getTelefono().trim())) {

            throw new IllegalArgumentException(
                    "El teléfono ya está registrado."
            );
        }
        /*Buscar Rol Cliente*/
        Rol rolCliente = rolRepository.findByNombreIgnoreCase("Cliente")
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró el rol Cliente en la base de datos."
                ));
        /*PROTEGEMOS LA CONTRASEÑA CON BCRYPT*/

        String contrasenaHash = passwordEncoder.encode(datos.getContrasena());

        /*Construir entidad usuarios*/
        Usuarios nuevoUsuario = new Usuarios();

        nuevoUsuario.setNombres(datos.getNombres().trim());
        nuevoUsuario.setApellidos(datos.getApellidos().trim());
        nuevoUsuario.setCorreo(datos.getCorreo().trim());
        nuevoUsuario.setTelefono(datos.getTelefono().trim());

        // GUARDAMOS EL HASH, NO LA CONTRASEÑA ORIGINAL
        nuevoUsuario.setPasswordHash(contrasenaHash);

        // ASIGNAR EL ROL DESDE EL BACKEND
        nuevoUsuario.setRol(rolCliente);

        // Estado inicial de la cuenta
        nuevoUsuario.setAceptaTerminos(true);
        nuevoUsuario.setRecibeNovedades(
                Boolean.TRUE.equals(datos.getRecibeNovedades())
        );
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setVerificado(false);

        /*Guardar user*/
        Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
        return convertirARespuestaDTO(usuarioGuardado);
    }
   /*Convertir la entida  en DTO respuesta*/
   private UsuarioRespuestaDTO convertirARespuestaDTO(Usuarios usuario) {

       return new UsuarioRespuestaDTO(
               usuario.getIdUsuario(),
               usuario.getNombres(),
               usuario.getApellidos(),
               usuario.getCorreo(),
               usuario.getTelefono(),
               usuario.getRol().getNombre(),
               usuario.getActivo(),
               usuario.getVerificado()
       );
   }

    /*Actualizar users*/
    public Optional<Usuarios> actualizarUsuario(
            Long idUsuario,
            Usuarios datosActualizados) {

        return usuariosRepository.findById(idUsuario)
                .map(usuarioExistente -> {

                    usuarioExistente.setNombres(datosActualizados.getNombres());
                    usuarioExistente.setApellidos(datosActualizados.getApellidos());
                    usuarioExistente.setCorreo(datosActualizados.getCorreo());
                    usuarioExistente.setTelefono(datosActualizados.getTelefono());
                    usuarioExistente.setRecibeNovedades(datosActualizados.getRecibeNovedades());

                    return usuariosRepository.save(usuarioExistente);
                });
    }
    /*Desactivar user*/
    public Optional<Usuarios> desactivarUsuario(Long idUsuario) {

        return usuariosRepository.findById(idUsuario)
                .map(usuario -> {
                    usuario.setActivo(false);
                    return usuariosRepository.save(usuario);
                });
    }
}
