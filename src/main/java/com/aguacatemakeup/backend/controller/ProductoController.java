
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Producto;
import com.aguacatemakeup.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Consultar todos los productos.
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        return ResponseEntity.ok(
                productoService.obtenerTodosLosProductos()
        );
    }

    // Consultar un producto por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productoService.obtenerProductoPorId(id)
        );
    }

    // Consultar únicamente productos publicados y activos.
    @GetMapping("/publicados")
    public ResponseEntity<List<Producto>> obtenerProductosPublicados() {
        return ResponseEntity.ok(
                productoService.obtenerProductosPublicados()
        );
    }

    // Consultar productos por subcategoría.
    @GetMapping("/subcategoria/{idSubcategoria}")
    public ResponseEntity<List<Producto>> obtenerPorSubcategoria(
            @PathVariable Long idSubcategoria) {

        return ResponseEntity.ok(
                productoService.obtenerProductosPorSubcategoria(idSubcategoria)
        );
    }

    // Crear un producto.
    @PostMapping
    public ResponseEntity<Producto> guardarProducto(
            @RequestBody Producto producto) {

        Producto nuevoProducto =
                productoService.guardarProducto(producto);

        return ResponseEntity.status(201).body(nuevoProducto);
    }

    // Actualizar un producto.
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return ResponseEntity.ok(
                productoService.actualizarProducto(id, producto)
        );
    }

    // Eliminar un producto.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {

        productoService.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }
}
