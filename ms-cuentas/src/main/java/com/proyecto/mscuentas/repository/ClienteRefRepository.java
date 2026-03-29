package com.proyecto.mscuentas.repository;

import com.proyecto.mscuentas.domain.ClienteRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRefRepository extends JpaRepository<ClienteRef, Long> {

    boolean existsByIdentificacion(String identificacion);

    boolean existsByIdentificacionAndClienteIdNot(String identificacion, Long clienteId);
}
