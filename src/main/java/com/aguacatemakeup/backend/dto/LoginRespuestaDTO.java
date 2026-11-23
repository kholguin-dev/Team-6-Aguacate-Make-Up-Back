
package com.aguacatemakeup.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/*DTO de respuesta del inicio de sesión.Devuelve el JWT y los datos públicos del usuario*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRespuestaDTO {

    // Token de autenticación generado por el backend.
    private String token;
    // El valor será Bearer.
    private String tipo;
    // Identificador del usuario autenticado.
    private Long idUsuario;
    // Nombre del usuario.
    private String nombres;
    // Correo del usuario.
    private String correo;
    // Rol asignado al usuario.
    private String rol;
}
