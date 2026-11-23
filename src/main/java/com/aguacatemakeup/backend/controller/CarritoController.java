
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Carrito;
import com.aguacatemakeup.backend.service.CarritoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Crear un carrito para un visitante.
    @PostMapping("/invitado")
    public ResponseEntity<Carrito> crearCarritoInvitado() {

        Carrito carrito = carritoService.crearCarritoInvitado();

        return ResponseEntity.status(201).body(carrito);
    }

    // Consultar el carrito de un visitante mediante su token.
    @GetMapping("/invitado/{token}")
    public ResponseEntity<Carrito> obtenerCarritoInvitado(
            @PathVariable String token) {

        return ResponseEntity.ok(
                carritoService.obtenerCarritoInvitado(token)
        );
    }

    // Consultar el carrito de un usuario registrado.
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Carrito> obtenerCarritoUsuario(
            @PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                carritoService.obtenerCarritoUsuario(idUsuario)
        );
    }

    // Obtener el carrito del usuario o crearlo si no existe.
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Carrito> obtenerOCrearCarritoUsuario(
            @PathVariable Long idUsuario) {

        Carrito carrito =
                carritoService.obtenerOCrearCarritoUsuario(idUsuario);

        return ResponseEntity.ok(carrito);
    }
}
