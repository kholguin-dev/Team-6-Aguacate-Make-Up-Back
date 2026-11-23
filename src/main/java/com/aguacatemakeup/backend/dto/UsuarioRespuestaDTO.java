
package com.aguacatemakeup.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRespuestaDTO {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String rol;
    private Boolean activo;
    private Boolean verificado;
}
