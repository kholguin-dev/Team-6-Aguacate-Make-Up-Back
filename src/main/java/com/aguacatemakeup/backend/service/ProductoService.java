
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Producto;
import com.aguacatemakeup.backend.entity.Subcategoria;
import com.aguacatemakeup.backend.entity.EstadoProducto;
import com.aguacatemakeup.backend.repository.ProductoRepository;
import com.aguacatemakeup.backend.repository.SubcategoriaRepository;
import com.aguacatemakeup.backend.repository.DetalleCarritoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            SubcategoriaRepository subcategoriaRepository,
            DetalleCarritoRepository detalleCarritoRepository) {

        this.productoRepository = productoRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
    }

    // 1. Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // 2. Obtener un producto por su ID
    public Producto obtenerProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));
    }

    // 3. Obtener únicamente productos publicados
    public List<Producto> obtenerProductosPublicados() {
        return productoRepository.findByEstadoAndActivoTrue(
                EstadoProducto.Publicado);
    }
    // 4. Obtener productos por subcategoría
    public List<Producto> obtenerProductosPorSubcategoria(
            Long idSubcategoria) {

        return productoRepository
                .findBySubcategoria_IdSubcategoria(idSubcategoria);
    }

    // 5. Registrar un producto
    @Transactional
    public Producto guardarProducto(Producto producto) {

        validarProducto(producto);
        if (productoRepository.existsByCodigoProducto(
                producto.getCodigoProducto())) {
            throw new RuntimeException(
                    "El código del producto ya está registrado");
        }
        if (productoRepository.existsBySku(producto.getSku())) {
            throw new RuntimeException(
                    "El SKU ya está registrado");
        }
        if (producto.getEstado() == null) {
            producto.setEstado(EstadoProducto.Borrador);
        }
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return productoRepository.save(producto);
    }

    // 6. Actualizar un producto
    @Transactional
    public Producto actualizarProducto(
            Long idProducto, Producto datosActualizados) {

        Producto productoExistente =
                obtenerProductoPorId(idProducto);

        validarProducto(datosActualizados);

        if (productoRepository
                .existsByCodigoProductoAndIdProductoNot(
                        datosActualizados.getCodigoProducto(),
                        idProducto)) {

            throw new RuntimeException(
                    "El código del producto ya está registrado");
        }

        if (productoRepository
                .existsBySkuAndIdProductoNot(
                        datosActualizados.getSku(),
                        idProducto)) {

            throw new RuntimeException(
                    "El SKU ya está registrado");
        }

        productoExistente.setCodigoProducto(
                datosActualizados.getCodigoProducto());

        productoExistente.setSku(datosActualizados.getSku());
        productoExistente.setSubcategoria(
                datosActualizados.getSubcategoria());
        productoExistente.setNombre(datosActualizados.getNombre());
        productoExistente.setMarca(datosActualizados.getMarca());
        productoExistente.setDescripcion(
                datosActualizados.getDescripcion());
        productoExistente.setPrecioBase(
                datosActualizados.getPrecioBase());
        productoExistente.setPrecioPromocional(
                datosActualizados.getPrecioPromocional());
        productoExistente.setStock(datosActualizados.getStock());
        productoExistente.setTamano(datosActualizados.getTamano());
        productoExistente.setEstado(datosActualizados.getEstado());
        productoExistente.setDestacado(datosActualizados.getDestacado());
        productoExistente.setNovedades(datosActualizados.getNovedades());
        productoExistente.setActivo(datosActualizados.getActivo());

        return productoRepository.save(productoExistente);
    }

    // 7. Eliminar un producto
    @Transactional
    public void eliminarProducto(Long idProducto) {

        Producto producto = obtenerProductoPorId(idProducto);

        boolean estaEnUnCarrito =
                detalleCarritoRepository
                        .existsByProducto_IdProducto(idProducto);

        if (estaEnUnCarrito) {
            throw new RuntimeException(
                    "No se puede eliminar el producto porque está asociado a un carrito");
        }

        productoRepository.delete(producto);
    }

    // Validaciones del producto
    private void validarProducto(Producto producto) {

        if (producto.getSubcategoria() == null
                || producto.getSubcategoria().getIdSubcategoria() == null) {

            throw new RuntimeException(
                    "El producto debe tener una subcategoría");
        }

        Long idSubcategoria =
                producto.getSubcategoria().getIdSubcategoria();

        Subcategoria subcategoria =
                subcategoriaRepository.findById(idSubcategoria)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "La subcategoría no existe"));

        producto.setSubcategoria(subcategoria);

        if (producto.getStock() == null
                || producto.getStock() < 0) {

            throw new RuntimeException(
                    "El stock no puede ser negativo ni nulo");
        }

        if (producto.getPrecioBase() == null
                || producto.getPrecioBase().signum() < 0) {

            throw new RuntimeException(
                    "El precio base debe ser mayor o igual a cero");
        }

        if (producto.getPrecioPromocional() != null
                && (producto.getPrecioPromocional().signum() < 0
                || producto.getPrecioPromocional()
                .compareTo(producto.getPrecioBase()) > 0)) {

            throw new RuntimeException(
                    "El precio promocional no puede ser negativo ni superar el precio base");
        }


    }
}
