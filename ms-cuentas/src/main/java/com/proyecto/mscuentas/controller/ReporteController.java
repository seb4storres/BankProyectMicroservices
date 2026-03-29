package com.proyecto.mscuentas.controller;

import com.proyecto.mscuentas.dto.ReporteDTO;
import com.proyecto.mscuentas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> generarReporte(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,
            @RequestParam(required = false) String fecha,
            @RequestParam Long clienteId) {

        if (fechaInicio == null || fechaFin == null) {
            LocalDate[] rango = parsearRango(fecha);
            fechaInicio = rango[0];
            fechaFin = rango[1];
        }

        return ResponseEntity.ok(reporteService.generarReporte(fechaInicio, fechaFin, clienteId));
    }

    private LocalDate[] parsearRango(String fecha) {
        if (!StringUtils.hasText(fecha)) {
            throw new IllegalArgumentException("Debe enviar fechaInicio/fechaFin o fecha en formato yyyy-MM-dd,yyyy-MM-dd");
        }

        String[] partes = fecha.split(",");
        if (partes.length != 2) {
            throw new IllegalArgumentException("El parámetro fecha debe tener formato yyyy-MM-dd,yyyy-MM-dd");
        }

        try {
            return new LocalDate[]{
                    LocalDate.parse(partes[0].trim()),
                    LocalDate.parse(partes[1].trim())
            };
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parámetro fecha debe tener formato yyyy-MM-dd,yyyy-MM-dd");
        }
    }
}

