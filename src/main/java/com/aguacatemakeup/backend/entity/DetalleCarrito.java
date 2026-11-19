
package com.aguacatemakeup.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "detalle_carrito",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_producto_carrito",
                        columnNames = {"carrito_id", "producto_id"}
                )
        }
)
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    // Muchos detalles pueden pertenecer al mismo carrito.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    // Muchos detalles pueden referenciar el mismo producto
    // en diferentes carritos.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // La cantidad debe ser un entero positivo.
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // MySQL asigna automáticamente la fecha.
    @Column(name = "fecha_agregado", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaAgregado;
}
