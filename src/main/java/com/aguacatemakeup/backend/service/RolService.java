package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aguacatemakeup.backend.entity.Rol;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RolService {
    private final RolRepository rolRepository;

    /*Método obtener roles*/
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    /*Método obtener roles x id*/

    public Optional<Rol> obtenerRolPorId(Integer idRol) {
        return rolRepository.findById(idRol);
    }

}
