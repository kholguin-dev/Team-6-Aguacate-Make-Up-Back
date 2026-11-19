
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
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    // Puede ser null cuando el mensaje lo envía un visitante osea el user id puede ser null
    // Un usuario puede enviar muchos mensajes.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @Column(name = "nombre_completo", length = 150, nullable = false)
    private String nombreCompleto;

    @Column(name = "correo", length = 150, nullable = false)
    private String correo;

    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Column(name = "mensaje", length = 500, nullable = false)
    private String mensaje;

    @Column(name = "acepta_politica_datos", nullable = false)
    private Boolean aceptaPoliticaDatos;

    // MySQL genera la fecha de solicitud.
    @Column(name = "fecha_solicitud", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaSolicitud;
}
