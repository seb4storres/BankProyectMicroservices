package com.proyecto.mscuentas.service.impl;

import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.domain.Movimiento;
import com.proyecto.mscuentas.dto.ReporteDTO;
import com.proyecto.mscuentas.exception.ResourceNotFoundException;
import com.proyecto.mscuentas.repository.ClienteRefRepository;
import com.proyecto.mscuentas.repository.MovimientoRepository;
import com.proyecto.mscuentas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final MovimientoRepository movimientoRepository;
    private final ClienteRefRepository clienteRefRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("El rango de fechas es inválido");
        }

        ClienteRef cliente = clienteRefRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + clienteId));

        List<Movimiento> movimientos = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin);

        return movimientos.stream().map(movimiento -> {
            BigDecimal saldoInicialMovimiento = movimiento.getSaldo().subtract(movimiento.getValor());
            return ReporteDTO.builder()
                    .fecha(movimiento.getFecha())
                    .cliente(cliente.getNombre())
                    .numeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                    .tipo(movimiento.getCuenta().getTipoCuenta())
                    .saldoInicial(saldoInicialMovimiento)
                    .estado(movimiento.getCuenta().getEstado())
                    .movimiento(movimiento.getValor())
                    .saldoDisponible(movimiento.getSaldo())
                    .build();
        }).toList();
    }
}
