
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.DetalleCarrito;
import com.aguacatemakeup.backend.service.DetalleCarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carritos/{idCarrito}/detalles")
@RequiredArgsConstructor
public class DetalleCarritoController {

    private final DetalleCarritoService detalleCarritoService;

    // Consultar los productos de un carrito.
    @GetMapping
    public ResponseEntity<List<DetalleCarrito>> obtenerDetallesPorCarrito(
            @PathVariable Long idCarrito) {

        return ResponseEntity.ok(
                detalleCarritoService.obtenerDetallesPorCarrito(idCarrito)
        );
    }
    // Agregar un producto al carrito.
    @PostMapping
    public ResponseEntity<DetalleCarrito> agregarProducto(
            @PathVariable Long idCarrito,
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad) {

        DetalleCarrito detalle = detalleCarritoService.agregarProducto(
                idCarrito,
                idProducto,
                cantidad
        );

        return ResponseEntity.status(201).body(detalle);
    }
    // Actualizar la cantidad de un producto.
    @PutMapping("/productos/{idProducto}")
    public ResponseEntity<DetalleCarrito> actualizarCantidad(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(
                detalleCarritoService.actualizarCantidad(
                        idCarrito,
                        idProducto,
                        cantidad
                )
        );
    }
    // Eliminar un producto específico del carrito.
    @DeleteMapping("/productos/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto) {

        detalleCarritoService.eliminarProducto(idCarrito, idProducto);

        return ResponseEntity.noContent().build();
    }
    // Vaciar todos los productos del carrito.
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable Long idCarrito) {

        detalleCarritoService.vaciarCarrito(idCarrito);

        return ResponseEntity.noContent().build();
    }
    // Revisar cuáles productos están agotados.
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<DetalleCarrito>> revisarDisponibilidadCarrito(
            @PathVariable Long idCarrito) {

        return ResponseEntity.ok(
                detalleCarritoService.revisarDisponibilidadCarrito(idCarrito)
        );
    }
    // Confirmar la eliminación de los productos agotados.
    @DeleteMapping("/productos-agotados")
    public ResponseEntity<Integer> eliminarProductosAgotadosConfirmados(
            @PathVariable Long idCarrito,
            @RequestParam boolean confirmado) {

        // Si el cliente no confirma, no modificamos el carrito.
        if (!confirmado) {
            return ResponseEntity.badRequest().build();
        }

        int cantidadEliminada =
                detalleCarritoService
                        .eliminarProductosAgotadosConfirmados(idCarrito);

        return ResponseEntity.ok(cantidadEliminada);
    }
}
