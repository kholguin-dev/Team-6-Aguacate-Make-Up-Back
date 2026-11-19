package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {
    boolean existsByProducto_IdProducto(Long idProducto);
}

