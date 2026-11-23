
package com.aguacatemakeup.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {

    // Datos personales
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;

    // Contraseña recibida desde el formulario
    private String contrasena;

    // Preferencias del usuario
    private Boolean aceptaTerminos;
    private Boolean recibeNovedades;
}
