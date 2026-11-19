
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
@Table(name = "imagenes_producto")
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    @Column(name = "texto_alternativo")
    private String textoAlternativo;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "fecha_creacion",
            insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}
