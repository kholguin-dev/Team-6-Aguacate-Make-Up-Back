package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Busca el carrito asociado a un usuario registrado.
    Optional<Carrito> findByUsuario_IdUsuario(Long idUsuario);

    // Busca el carrito asociado a un visitante mediante su token.
    Optional<Carrito> findByTokenInvitado(String tokenInvitado);
}

