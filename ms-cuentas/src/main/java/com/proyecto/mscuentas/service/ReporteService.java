package com.proyecto.mscuentas.service;

import com.proyecto.mscuentas.dto.ReporteDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    List<ReporteDTO> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId);
}

