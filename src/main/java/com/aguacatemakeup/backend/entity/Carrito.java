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
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;

    // Un carrito puede pertenecer a un usuario registrado.
    // Es OneToOne porque la base de datos tiene UNIQUE(usuario_id).
    // Es opcional porque los visitantes no tienen usuario registrado.

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    // Identificador del carrito cuando el propietario es un visitante.
    @Column(name = "token_invitado", length = 36)
    private String tokenInvitado;

    // La fecha la genera MySQL automáticamente.
    @Column(name = "fecha_creacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // MySQL actualiza este campo cuando se modifica el registro.
    @Column(name = "fecha_actualizacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;
}
