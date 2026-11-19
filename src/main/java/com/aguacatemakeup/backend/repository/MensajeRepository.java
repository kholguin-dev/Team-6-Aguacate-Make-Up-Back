package com.aguacatemakeup.backend.repository;

import com.aguacatemakeup.backend.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

}

