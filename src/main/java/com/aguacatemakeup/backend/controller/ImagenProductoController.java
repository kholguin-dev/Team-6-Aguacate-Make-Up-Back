
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.ImagenProducto;
import com.aguacatemakeup.backend.service.ImagenProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImagenProductoController {

    private final ImagenProductoService imagenProductoService;

    // 1. Consultar las imágenes de un producto.
    @GetMapping("/productos/{idProducto}/imagenes")
    public ResponseEntity<List<ImagenProducto>> obtenerImagenesPorProducto(
            @PathVariable Long idProducto) {

        List<ImagenProducto> imagenes =
                imagenProductoService.obtenerImagenesPorProducto(idProducto);

        return ResponseEntity.ok(imagenes);
    }

    // 2. Registrar una imagen.
    @PostMapping("/imagenes-producto")
    public ResponseEntity<ImagenProducto> guardarImagen(
            @RequestBody ImagenProducto imagen) {

        ImagenProducto nuevaImagen =
                imagenProductoService.guardarImagen(imagen);

        return ResponseEntity.status(201).body(nuevaImagen);
    }

    // 3. Actualizar una imagen.
    @PutMapping("/imagenes-producto/{idImagen}")
    public ResponseEntity<ImagenProducto> actualizarImagen(
            @PathVariable Long idImagen,
            @RequestBody ImagenProducto datosActualizados) {

        ImagenProducto imagenActualizada =
                imagenProductoService.actualizarImagen(
                        idImagen,
                        datosActualizados
                );

        return ResponseEntity.ok(imagenActualizada);
    }

    // 4. Eliminar una imagen.
    @DeleteMapping("/imagenes-producto/{idImagen}")
    public ResponseEntity<Void> eliminarImagen(
            @PathVariable Long idImagen) {

        imagenProductoService.eliminarImagen(idImagen);

        return ResponseEntity.noContent().build();
    }

    // 5. Establecer una imagen como principal.
    @PatchMapping("/imagenes-producto/{idImagen}/principal")
    public ResponseEntity<ImagenProducto> establecerImagenPrincipal(
            @PathVariable Long idImagen) {

        ImagenProducto imagenPrincipal =
                imagenProductoService.establecerImagenPrincipal(idImagen);

        return ResponseEntity.ok(imagenPrincipal);
    }
}
