package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagenProductoRepository
        extends JpaRepository<ImagenProducto, Long> {

    // Obtener todas las imágenes de un producto
    List<ImagenProducto> findByProducto_IdProducto(Long idProducto);

    // Contar cuántas imágenes tiene un producto
    long countByProducto_IdProducto(Long idProducto);

    // Buscar la imagen principal de un producto
    Optional<ImagenProducto> findByProducto_IdProductoAndEsPrincipalTrue(
            Long idProducto);

    // Verificar si una imagen principal existe, excluyendo una imagen
    boolean existsByProducto_IdProductoAndEsPrincipalTrueAndIdImagenNot(
            Long idProducto, Long idImagen);
}