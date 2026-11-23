package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {
    boolean existsByProducto_IdProducto(Long idProducto);

    // Consulta todos los productos que pertenecen a un carrito.
    List<DetalleCarrito> findByCarrito_IdCarrito(Long idCarrito);

    // Busca un producto específico dentro de un carrito.
    Optional<DetalleCarrito> findByCarrito_IdCarritoAndProducto_IdProducto(
            Long idCarrito,
            Long idProducto
    );
}

