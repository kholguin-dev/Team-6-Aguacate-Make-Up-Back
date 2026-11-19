
package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    // Verificar si una categoría tiene subcategorías
    boolean existsByCategoria_IdCategoria(Long idCategoria);

    // Buscar subcategorías por categoría
    List<Subcategoria> findByCategoria_IdCategoria(Long idCategoria);

    // Verificar si ya existe el nombre dentro de una categoría
    boolean existsByCategoria_IdCategoriaAndNombre(
            Long idCategoria, String nombre
    );

    // Verificar nombres duplicados excluyendo la subcategoría actual
    boolean existsByCategoria_IdCategoriaAndNombreAndIdSubcategoriaNot(
            Long idCategoria, String nombre, Long idSubcategoria
    );
}

