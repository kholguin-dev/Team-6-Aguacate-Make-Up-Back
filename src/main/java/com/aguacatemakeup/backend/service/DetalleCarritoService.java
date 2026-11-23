
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Carrito;
import com.aguacatemakeup.backend.entity.DetalleCarrito;
import com.aguacatemakeup.backend.entity.Producto;
import com.aguacatemakeup.backend.entity.EstadoProducto;

import com.aguacatemakeup.backend.repository.CarritoRepository;
import com.aguacatemakeup.backend.repository.DetalleCarritoRepository;
import com.aguacatemakeup.backend.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetalleCarritoService {

    private final DetalleCarritoRepository detalleCarritoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    // Inyección de dependencias por constructor.
    public DetalleCarritoService(
            DetalleCarritoRepository detalleCarritoRepository,
            CarritoRepository carritoRepository,
            ProductoRepository productoRepository) {

        this.detalleCarritoRepository = detalleCarritoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    // 1. Consultar todos los productos de un carrito.
    @Transactional(readOnly = true)
    public List<DetalleCarrito> obtenerDetallesPorCarrito(Long idCarrito) {

        // Verificamos que el carrito exista.
        if (!carritoRepository.existsById(idCarrito)) {
            throw new RuntimeException("Carrito no encontrado");
        }

        // Obtenemos todos los detalles asociados al carrito.
        return detalleCarritoRepository.findByCarrito_IdCarrito(idCarrito);
    }

    // 2. Agregar un producto al carrito.
    @Transactional
    public DetalleCarrito agregarProducto(
            Long idCarrito,
            Long idProducto,
            Integer cantidad) {

        // Validamos que la cantidad sea positiva.
        validarCantidad(cantidad);

        // Buscamos el carrito.
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException(
                        "Carrito no encontrado"
                ));

        // Buscamos el producto.
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException(
                        "Producto no encontrado"
                ));

        // Verificamos que el producto esté publicado y activo.
        validarDisponibilidadProducto(producto);

        // Revisamos si el producto ya está en el carrito.
        return detalleCarritoRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(
                        idCarrito,
                        idProducto
                )
                .map(detalleExistente -> {

                    // Si ya existe, sumamos la nueva cantidad.
                    detalleExistente.setCantidad(
                            detalleExistente.getCantidad() + cantidad
                    );

                    return detalleCarritoRepository.save(detalleExistente);
                })
                .orElseGet(() -> {

                    // Si no existe, creamos un nuevo detalle.
                    DetalleCarrito nuevoDetalle = new DetalleCarrito();

                    nuevoDetalle.setCarrito(carrito);
                    nuevoDetalle.setProducto(producto);
                    nuevoDetalle.setCantidad(cantidad);

                    return detalleCarritoRepository.save(nuevoDetalle);
                });
    }

    // 3. Actualizar la cantidad de un producto del carrito.
    @Transactional
    public DetalleCarrito actualizarCantidad(
            Long idCarrito,
            Long idProducto,
            Integer nuevaCantidad) {

        // Validamos que la nueva cantidad sea positiva.
        validarCantidad(nuevaCantidad);

        // Buscamos el detalle del producto dentro del carrito.
        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(
                        idCarrito,
                        idProducto
                )
                .orElseThrow(() -> new RuntimeException(
                        "El producto no existe en este carrito"
                ));

        // Reemplazamos la cantidad anterior por la nueva.
        detalle.setCantidad(nuevaCantidad);

        return detalleCarritoRepository.save(detalle);
    }

    // 4. Eliminar un producto específico del carrito.
    @Transactional
    public void eliminarProducto(Long idCarrito, Long idProducto) {

        // Buscamos el detalle correspondiente.
        DetalleCarrito detalle = detalleCarritoRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(
                        idCarrito,
                        idProducto
                )
                .orElseThrow(() -> new RuntimeException(
                        "El producto no existe en este carrito"
                ));

        // Eliminamos solamente ese detalle.
        detalleCarritoRepository.delete(detalle);
    }

    // 5. Vaciar todos los productos de un carrito.
    @Transactional
    public void vaciarCarrito(Long idCarrito) {

        // Verificamos que el carrito exista.
        if (!carritoRepository.existsById(idCarrito)) {
            throw new RuntimeException("Carrito no encontrado");
        }

        // Consultamos los detalles del carrito.
        List<DetalleCarrito> detalles =
                detalleCarritoRepository.findByCarrito_IdCarrito(idCarrito);

        // Eliminamos los detalles, no el carrito.
        detalleCarritoRepository.deleteAll(detalles);
    }

    // Validación reutilizable de cantidades.
    private void validarCantidad(Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero"
            );
        }
    }

    // Validación reutilizable del estado del producto.

    private void validarDisponibilidadProducto(Producto producto) {

        if (!Boolean.TRUE.equals(producto.getActivo())
                || producto.getEstado() != EstadoProducto.Publicado) {

            throw new IllegalArgumentException(
                    "El producto no está disponible para agregar al carrito"
            );
        }

        if (producto.getStock() == null || producto.getStock() <= 0) {

            throw new IllegalArgumentException(
                    "El producto está agotado y no se puede agregar al carrito"
            );
        }
    }

// 6. Revisar cuáles productos del carrito están agotados.
            @Transactional(readOnly = true)
            public List<DetalleCarrito> revisarDisponibilidadCarrito(Long idCarrito) {

                // Verificamos que el carrito exista.
                if (!carritoRepository.existsById(idCarrito)) {
                    throw new RuntimeException("Carrito no encontrado");
                }

                // Consultamos todos los productos del carrito.
                List<DetalleCarrito> detalles =
                        detalleCarritoRepository.findByCarrito_IdCarrito(idCarrito);

                // Filtramos los productos cuyo stock es cero o menor.
                return detalles.stream()
                        .filter(detalle ->
                                detalle.getProducto().getStock() == null
                                        || detalle.getProducto().getStock() <= 0
                        )
                        .toList();
            }

// 7. Eliminar los productos agotados cuando el cliente confirma.
            @Transactional
            public int eliminarProductosAgotadosConfirmados(Long idCarrito) {

                // Verificamos que el carrito exista.
                if (!carritoRepository.existsById(idCarrito)) {
                    throw new RuntimeException("Carrito no encontrado");
                }

                // Volvemos a consultar los detalles actuales del carrito.
                List<DetalleCarrito> detalles =
                        detalleCarritoRepository.findByCarrito_IdCarrito(idCarrito);

                // Volvemos a comprobar el stock.
                // Solo seleccionamos los productos que continúan agotados.
                List<DetalleCarrito> detallesAgotados = detalles.stream()
                        .filter(detalle ->
                                detalle.getProducto().getStock() == null
                                        || detalle.getProducto().getStock() <= 0
                        )
                        .toList();

                // Eliminamos únicamente los detalles agotados.
                detalleCarritoRepository.deleteAll(detallesAgotados);

                // Devolvemos cuántos productos fueron eliminados.
                return detallesAgotados.size();
            }
}
