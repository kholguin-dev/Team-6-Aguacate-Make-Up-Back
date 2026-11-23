
package com.aguacatemakeup.backend.controller;

/* DTO */
import com.aguacatemakeup.backend.dto.LoginRequestDTO;
import com.aguacatemakeup.backend.dto.LoginRespuestaDTO;

import com.aguacatemakeup.backend.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* @RestController:  Indica que esta clase recibe solicitudes REST y devuelve respuestas, normalmente en JSON.
 * @RequestMapping: Define la ruta base del Controller.
 *@RequiredArgsConstructor: Genera el constructor para inyectar AuthService. */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Servicio encargado de validar las credenciales.
    private final AuthService authService;


    /* ENDPOINT DE INICIO DE SESIÓN :
     * @RequestBody:Convierte el JSON recibido en LoginRequestDTO.
     * ResponseEntity: Permite controlar el código HTTP de la respuesta. */

    @PostMapping("/login")
    public ResponseEntity<LoginRespuestaDTO> iniciarSesion(
            @RequestBody LoginRequestDTO datos) {
        // Delegamos la autenticación al servicio.
        LoginRespuestaDTO respuesta =
                authService.iniciarSesion(datos);
        // Devolvemos 200 OK con el token y los datos del usuario.
        return ResponseEntity.ok(respuesta);

    }
}

