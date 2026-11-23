
package com.aguacatemakeup.backend.service;
import com.aguacatemakeup.backend.entity.Usuarios;

/* DTO*/
import com.aguacatemakeup.backend.dto.LoginRequestDTO;
import com.aguacatemakeup.backend.dto.LoginRespuestaDTO;

import com.aguacatemakeup.backend.repository.UsuariosRepository;

/* SPRING SECURITY */
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/* jwt */
import com.aguacatemakeup.backend.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    /* DEPENDENCIAS */

    // Permite buscar al usuario registrado en MySQL.
    private final UsuariosRepository usuariosRepository;
    // Permite comparar la contraseña ingresada con el hash.
    private final PasswordEncoder passwordEncoder;
    // Permitirá generar el token JWT.
    private final JwtService jwtService;


    /* INICIAR SESIÓN */

    public LoginRespuestaDTO iniciarSesion(LoginRequestDTO datos) {
        /* 1. Valida datos recibidos  */

        if (datos == null
                || datos.getCorreo() == null
                || datos.getCorreo().isBlank()
                || datos.getContrasena() == null
                || datos.getContrasena().isBlank()) {

            throw new IllegalArgumentException(
                    "El correo y la contraseña son obligatorios."
            );
        }

        /* 2. Busca user x mail  */
        Usuarios usuario = usuariosRepository
                .findByCorreo(datos.getCorreo().trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Correo o contraseña incorrectos."
                ));


        /* 3. Valida estado de la cuenta */
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "La cuenta se encuentra desactivada."
            );
        }

        /* 4. COMPARAR CONTRASEÑA CON BCRYPT:  No generamos un hash nuevo para comparar con matches. */
        boolean contrasenaCorrecta = passwordEncoder.matches(
                datos.getContrasena(),
                usuario.getPasswordHash()
        );
        if (!contrasenaCorrecta) {
            throw new IllegalArgumentException(
                    "Correo o contraseña incorrectos."
            );
        }

        /* 5. GENERAR TOKEN JWT: Si las credenciales son correctas */
        String token = jwtService.generarToken(usuario);

        /* 6. CONSTRUIR RESPUESTA: Devolvemos el token: Nunca devolvemos la contraseña ni su hash.  */

        return new LoginRespuestaDTO(
                token,
                "Bearer",
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol().getNombre()
        );

    }

}
