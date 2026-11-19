package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Usuarios;
import com.aguacatemakeup.backend.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

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
    /*Guardar user*/
    public Usuarios guardarUsuario(Usuarios usuario) {
        return usuariosRepository.save(usuario);
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
