
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Rol;
import com.aguacatemakeup.backend.service.RolService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    // 1. Consultar todos los roles.
    @GetMapping
    public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
        List<Rol> roles = rolService.obtenerTodosLosRoles();
        return ResponseEntity.ok(roles);
    }

    // 2. Consultar un rol por su identificador.
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(
            @PathVariable Integer id) {

        return rolService.obtenerRolPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
