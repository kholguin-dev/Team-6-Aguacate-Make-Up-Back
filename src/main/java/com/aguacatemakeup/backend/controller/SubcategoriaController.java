
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Subcategoria;
import com.aguacatemakeup.backend.service.SubcategoriaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategorias")
@RequiredArgsConstructor
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    // Consultar todas las subcategorías.
    @GetMapping
    public ResponseEntity<List<Subcategoria>> obtenerTodasLasSubcategorias() {
        return ResponseEntity.ok(
                subcategoriaService.obtenerTodasLasSubcategorias()
        );
    }
    // Consultar una subcategoría por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Subcategoria> obtenerSubcategoriaPorId(
            @PathVariable Long id) {

        return subcategoriaService.obtenerSubcategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Consultar subcategorías de una categoría.
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Subcategoria>> obtenerPorCategoria(
            @PathVariable Long idCategoria) {

        return ResponseEntity.ok(
                subcategoriaService.obtenerSubcategoriasPorCategoria(idCategoria)
        );
    }
    // Crear una subcategoría.
    @PostMapping
    public ResponseEntity<Subcategoria> guardarSubcategoria(
            @RequestBody Subcategoria subcategoria) {

        Subcategoria nuevaSubcategoria =
                subcategoriaService.guardarSubcategoria(subcategoria);

        return ResponseEntity.status(201).body(nuevaSubcategoria);
    }
    // Actualizar una subcategoría.
    @PutMapping("/{id}")
    public ResponseEntity<Subcategoria> actualizarSubcategoria(
            @PathVariable Long id,
            @RequestBody Subcategoria subcategoria) {

        return subcategoriaService.actualizarSubcategoria(id, subcategoria)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Eliminar una subcategoría.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubcategoria(
            @PathVariable Long id) {

        subcategoriaService.eliminarSubcategoria(id);

        return ResponseEntity.noContent().build();
    }
}
