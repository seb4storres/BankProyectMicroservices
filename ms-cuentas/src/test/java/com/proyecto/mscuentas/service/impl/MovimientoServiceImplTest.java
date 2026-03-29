package com.proyecto.mscuentas.service.impl;

import com.proyecto.mscuentas.domain.Cuenta;
import com.proyecto.mscuentas.domain.Movimiento;
import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.dto.MovimientoResponseDTO;
import com.proyecto.mscuentas.exception.SaldoInsuficienteException;
import com.proyecto.mscuentas.mapper.MovimientoMapper;
import com.proyecto.mscuentas.repository.CuentaRepository;
import com.proyecto.mscuentas.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    private MovimientoServiceImpl movimientoService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        movimientoService = new MovimientoServiceImpl(movimientoRepository, cuentaRepository, new MovimientoMapper());

        cuenta = Cuenta.builder()
                .id(10L)
                .numeroCuenta("225487")
                .tipoCuenta("Corriente")
                .saldoInicial(new BigDecimal("100.00"))
                .estado(true)
                .clienteId(1L)
                .build();
    }

    @Test
    void shouldCreateMovimientoAndUpdateCuentaSaldo() {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .numeroCuenta("225487")
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("600.00"))
                .build();

        when(cuentaRepository.findByNumeroCuenta("225487")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(invocation -> {
            Movimiento m = invocation.getArgument(0);
            m.setId(1L);
            return m;
        });
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoResponseDTO response = movimientoService.create(request);

        assertEquals(new BigDecimal("700.00"), response.getSaldo());
        assertEquals(new BigDecimal("700.00"), cuenta.getSaldoInicial());
    }

    @Test
    void shouldThrowWhenSaldoInsuficiente() {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .numeroCuenta("225487")
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("-101.00"))
                .build();

        when(cuentaRepository.findByNumeroCuenta("225487")).thenReturn(Optional.of(cuenta));

        SaldoInsuficienteException ex = assertThrows(SaldoInsuficienteException.class, () -> movimientoService.create(request));
        assertEquals("Saldo no disponible", ex.getMessage());
    }
}
