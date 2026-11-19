package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aguacatemakeup.backend.entity.EstadoProducto;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsBySubcategoria_IdSubcategoria(Long idSubcategoria);
    boolean existsByCodigoProducto(String codigoProducto);

    boolean existsBySku(String sku);

    boolean existsByCodigoProductoAndIdProductoNot(
            String codigoProducto, Long idProducto);

    boolean existsBySkuAndIdProductoNot(
            String sku, Long idProducto);

    List<Producto> findByEstado(EstadoProducto estado);
    List<Producto> findByEstadoAndActivoTrue(EstadoProducto estado);
    List<Producto> findBySubcategoria_IdSubcategoria(Long idSubcategoria);
}