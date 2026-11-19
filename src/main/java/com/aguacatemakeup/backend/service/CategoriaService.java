
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Categoria;
import com.aguacatemakeup.backend.repository.CategoriaRepository;
import com.aguacatemakeup.backend.repository.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    // Consultar todas las categorías
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    // Consultar una categoría por su ID
    public Optional<Categoria> obtenerCategoriaPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    // Registrar una nueva categoría
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Actualizar una categoría existente
    public Optional<Categoria> actualizarCategoria(
            Long idCategoria, Categoria datosActualizados) {

        return categoriaRepository.findById(idCategoria)
                .map(categoriaExistente -> {

                    categoriaExistente.setNombre(datosActualizados.getNombre());
                    categoriaExistente.setDescripcion(datosActualizados.getDescripcion());
                    categoriaExistente.setActivo(datosActualizados.getActivo());

                    return categoriaRepository.save(categoriaExistente);
                });
    }

    // Eliminar una categoría si no tiene subcategorías
    public void eliminarCategoria(Long idCategoria) {

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new IllegalArgumentException("La categoría no existe"));

        boolean tieneSubcategorias =
                subcategoriaRepository.existsByCategoria_IdCategoria(idCategoria);

        if (tieneSubcategorias) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría porque tiene subcategorías asociadas");
        }

        categoriaRepository.delete(categoria);
    }
}
