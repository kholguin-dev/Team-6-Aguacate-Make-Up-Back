
package com.aguacatemakeup.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/*Identificamos la tabla*/

@Entity
@Table(name = "productos")
public class Producto {

    /*dentificador del producto*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    /*Identificamos los datos propios  del producto*/


    @Column(name = "codigo_producto", length = 20,
            nullable = false, unique = true)
    private String codigoProducto;

    @Column(name = "sku", length = 45,
            nullable = false, unique = true)
    private String sku;

    /*Relación con subcategorías*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private Subcategoria subcategoria;


    /*Info descriptiva*/


    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column(name = "marca", length = 100, nullable = false)
    private String marca;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /*Precios*/

    @Column(name = "precio_base", precision = 12, scale = 2,
            nullable = false)
    private BigDecimal precioBase;

    @Column(name = "precio_promocional", precision = 12, scale = 2)
    private BigDecimal precioPromocional;

    /*Stock y Tamaño*/

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "tamano", length = 50)
    private String tamano;

    /*Estado del producto*/

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false,
            columnDefinition = "ENUM('Borrador','Publicado')")
    private EstadoProducto estado;


    /*Indicadores del producto*/

    @Column(name = "destacado", nullable = false)
    private Boolean destacado;

    @Column(name = "novedades", nullable = false)
    private Boolean novedades;

    @Column(name = "activo", nullable = false)
    private Boolean activo;


    /*Fechas*/

    @Column(name = "fecha_creacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;


}
