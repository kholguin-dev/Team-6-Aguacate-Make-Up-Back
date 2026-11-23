package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByCorreo(String correo);
    boolean existsByTelefono(String telefono);
}

