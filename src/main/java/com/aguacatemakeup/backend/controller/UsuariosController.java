
package com.aguacatemakeup.backend.controller;

import com.aguacatemakeup.backend.entity.Usuarios;
import com.aguacatemakeup.backend.service.UsuariosService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuariosService usuariosService;

    // 1. Consultar usuarios con paginación.
    @GetMapping
    public ResponseEntity<Page<Usuarios>> obtenerTodosLosUsuarios(
            @PageableDefault(size = 10, sort = "idUsuario")
            Pageable pageable) {

        return ResponseEntity.ok(
                usuariosService.obtenerTodosLosUsuarios(pageable)
        );
    }

    // 2. Consultar un usuario por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerUsuarioPorId(
            @PathVariable Long id) {

        return usuariosService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Buscar un usuario por correo.
    @GetMapping("/correo")
    public ResponseEntity<Usuarios> buscarUsuarioPorCorreo(
            @RequestParam String correo) {

        return usuariosService.buscarUsuarioPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Registrar un usuario.
    @PostMapping
    public ResponseEntity<Usuarios> guardarUsuario(
            @RequestBody Usuarios usuario) {

        Usuarios nuevoUsuario =
                usuariosService.guardarUsuario(usuario);

        return ResponseEntity.status(201).body(nuevoUsuario);
    }

    // 5. Actualizar los datos de un usuario.
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuarios datosActualizados) {

        return usuariosService.actualizarUsuario(id, datosActualizados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 6. Desactivar un usuario.
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Usuarios> desactivarUsuario(
            @PathVariable Long id) {

        return usuariosService.desactivarUsuario(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
