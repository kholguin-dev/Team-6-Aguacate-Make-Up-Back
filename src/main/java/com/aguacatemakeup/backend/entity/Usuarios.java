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
@Table(name = "usuarios")

public class Usuarios {

    /*Relación rol con Rol*/

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;


    /*Datos personales.*/

    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "correo", length = 150, nullable = false, unique = true)
    private String correo;

    @Column(name = "telefono", length = 20, nullable = false, unique = true)
    private String telefono;

    /*Credenciales*/


    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;
    @Column(name = "acepta_terminos", nullable = false)
    private Boolean aceptaTerminos;


    /*Preferencias y estados.*/

    @Column(name = "recibe_novedades", nullable = false)
    private Boolean recibeNovedades;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

    /*Fechas*/

    @Column(name = "fecha_registro", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;
}