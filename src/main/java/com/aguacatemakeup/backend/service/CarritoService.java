
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Carrito;
import com.aguacatemakeup.backend.entity.Usuarios;
import com.aguacatemakeup.backend.repository.CarritoRepository;
import com.aguacatemakeup.backend.repository.UsuariosRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuariosRepository usuariosRepository;

    // Inyección de dependencias por constructor.
    public CarritoService(
            CarritoRepository carritoRepository,
            UsuariosRepository usuariosRepository) {

        this.carritoRepository = carritoRepository;
        this.usuariosRepository = usuariosRepository;
    }

    // 1. Crear un carrito para un visitante.
    @Transactional
    public Carrito crearCarritoInvitado() {

        Carrito carrito = new Carrito();

        // Genera un identificador único para el visitante.
        carrito.setTokenInvitado(UUID.randomUUID().toString());

        // El visitante no tiene un usuario registrado.
        carrito.setUsuario(null);

        return carritoRepository.save(carrito);
    }

    // 2. Buscar el carrito de un visitante por su token.
    @Transactional(readOnly = true)
    public Carrito obtenerCarritoInvitado(String tokenInvitado) {

        if (tokenInvitado == null || tokenInvitado.isBlank()) {
            throw new IllegalArgumentException(
                    "El token del visitante es obligatorio"
            );
        }

        return carritoRepository.findByTokenInvitado(tokenInvitado)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el carrito del visitante"
                ));
    }

    // 3. Buscar el carrito de un usuario registrado.
    @Transactional(readOnly = true)
    public Carrito obtenerCarritoUsuario(Long idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El identificador del usuario es obligatorio"
            );
        }

        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "El usuario todavía no tiene un carrito"
                ));
    }

    // 4. Obtener el carrito del usuario o crear uno si no existe.
    @Transactional
    public Carrito obtenerOCrearCarritoUsuario(Long idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El identificador del usuario es obligatorio"
            );
        }

        // Primero verificamos si el usuario ya tiene un carrito.
        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseGet(() -> {

                    // Verificamos que el usuario exista.
                    Usuarios usuario = usuariosRepository.findById(idUsuario)
                            .orElseThrow(() -> new RuntimeException(
                                    "No se encontró el usuario"
                            ));

                    // Creamos un nuevo carrito asociado al usuario.
                    Carrito carrito = new Carrito();
                    carrito.setUsuario(usuario);
                    carrito.setTokenInvitado(null);

                    return carritoRepository.save(carrito);
                });
    }
}
