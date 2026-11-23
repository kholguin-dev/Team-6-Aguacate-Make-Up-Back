
package com.aguacatemakeup.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/* DTO de solicitud de inicio de sesión.Recibe las credenciales que envía el usuario */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    // Correo electrónico utilizado para identificar al usuario.
    private String correo;

    // Contraseña original que escribe el usuario en el login.
    private String contrasena;
}
