package com.proyecto.mscuentas.repository;

import com.proyecto.mscuentas.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaDescIdDesc(Long cuentaId);

    @Query("""
            SELECT m FROM Movimiento m
            JOIN FETCH m.cuenta c
            WHERE c.clienteId = :clienteId
              AND m.fecha BETWEEN :fechaInicio AND :fechaFin
            ORDER BY m.fecha DESC
            """)
    List<Movimiento> findByClienteIdAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}