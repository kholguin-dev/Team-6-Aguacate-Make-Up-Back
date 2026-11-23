
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Categoria;
import com.aguacatemakeup.backend.service.CategoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;
    // Consultar todas las categorías.
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        return ResponseEntity.ok(
                categoriaService.obtenerTodasLasCategorias()
        );
    }
    // Consultar una categoría por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(
            @PathVariable Long id) {

        return categoriaService.obtenerCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Crear una categoría.
    @PostMapping
    public ResponseEntity<Categoria> guardarCategoria(
            @RequestBody Categoria categoria) {
        Categoria nuevaCategoria =
                categoriaService.guardarCategoria(categoria);
        return ResponseEntity.status(201).body(nuevaCategoria);
    }
    // Actualizar una categoría.
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody Categoria categoria) {

        return categoriaService.actualizarCategoria(id, categoria)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Eliminar una categoría.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
