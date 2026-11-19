
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Categoria;
import com.aguacatemakeup.backend.entity.Subcategoria;
import com.aguacatemakeup.backend.repository.CategoriaRepository;
import com.aguacatemakeup.backend.repository.SubcategoriaRepository;
import com.aguacatemakeup.backend.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    // 1. Consultar todas las subcategorías
    public List<Subcategoria> obtenerTodasLasSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    // 2. Consultar una subcategoría por su ID
    public Optional<Subcategoria> obtenerSubcategoriaPorId(
            Long idSubcategoria) {

        return subcategoriaRepository.findById(idSubcategoria);
    }

    // 3. Consultar subcategorías pertenecientes a una categoría
    public List<Subcategoria> obtenerSubcategoriasPorCategoria(
            Long idCategoria) {

        return subcategoriaRepository
                .findByCategoria_IdCategoria(idCategoria);
    }

    // 4. Registrar una nueva subcategoría
    public Subcategoria guardarSubcategoria(Subcategoria subcategoria) {

        Long idCategoria = subcategoria.getCategoria().getIdCategoria();

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La categoría indicada no existe"
                        )
                );

        boolean nombreDuplicado =
                subcategoriaRepository
                        .existsByCategoria_IdCategoriaAndNombre(
                                idCategoria,
                                subcategoria.getNombre()
                        );

        if (nombreDuplicado) {
            throw new IllegalArgumentException(
                    "Ya existe una subcategoría con ese nombre en esta categoría"
            );
        }

        subcategoria.setCategoria(categoria);

        if (subcategoria.getActivo() == null) {
            subcategoria.setActivo(true);
        }

        return subcategoriaRepository.save(subcategoria);
    }

    // 5. Actualizar una subcategoría existente
    public Optional<Subcategoria> actualizarSubcategoria(
            Long idSubcategoria,
            Subcategoria datosActualizados) {

        return subcategoriaRepository.findById(idSubcategoria)
                .map(subcategoriaExistente -> {

                    Long idCategoria = datosActualizados
                            .getCategoria()
                            .getIdCategoria();

                    Categoria categoria = categoriaRepository
                            .findById(idCategoria)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "La categoría indicada no existe"
                                    )
                            );

                    boolean nombreDuplicado =
                            subcategoriaRepository
                                    .existsByCategoria_IdCategoriaAndNombreAndIdSubcategoriaNot(
                                            idCategoria,
                                            datosActualizados.getNombre(),
                                            idSubcategoria
                                    );

                    if (nombreDuplicado) {
                        throw new IllegalArgumentException(
                                "Ya existe otra subcategoría con ese nombre en esta categoría"
                        );
                    }

                    subcategoriaExistente.setCategoria(categoria);
                    subcategoriaExistente.setNombre(
                            datosActualizados.getNombre()
                    );
                    subcategoriaExistente.setActivo(
                            datosActualizados.getActivo()
                    );

                    return subcategoriaRepository.save(
                            subcategoriaExistente
                    );
                });
    }

    // 6. Eliminar una subcategoría
    public void eliminarSubcategoria(Long idSubcategoria) {

        Subcategoria subcategoria = subcategoriaRepository
                .findById(idSubcategoria)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La subcategoría no existe"
                        )
                );

        boolean tieneProductos = productoRepository
                .existsBySubcategoria_IdSubcategoria(idSubcategoria);

        if (tieneProductos) {
            throw new IllegalStateException(
                    "No se puede eliminar la subcategoría porque tiene productos asociados"
            );
        }

        subcategoriaRepository.delete(subcategoria);
    }
}
