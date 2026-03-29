package com.proyecto.mscuentas.service.impl;

import com.proyecto.mscuentas.domain.Cuenta;
import com.proyecto.mscuentas.domain.Movimiento;
import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.dto.MovimientoResponseDTO;
import com.proyecto.mscuentas.exception.ResourceNotFoundException;
import com.proyecto.mscuentas.exception.SaldoInsuficienteException;
import com.proyecto.mscuentas.mapper.MovimientoMapper;
import com.proyecto.mscuentas.repository.CuentaRepository;
import com.proyecto.mscuentas.repository.MovimientoRepository;
import com.proyecto.mscuentas.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> findAll() {
        return movimientoRepository.findAll().stream()
                .map(movimientoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponseDTO findById(Long id) {
        Movimiento movimiento = getMovimientoOrThrow(id);
        return movimientoMapper.toResponseDTO(movimiento);
    }

    @Override
    @Transactional
    public MovimientoResponseDTO create(MovimientoRequestDTO requestDTO) {
        Cuenta cuenta = getCuentaByNumeroOrThrow(requestDTO.getNumeroCuenta());
        BigDecimal nuevoSaldo = calcularNuevoSaldo(cuenta.getSaldoInicial(), requestDTO.getValor());

        Movimiento movimiento = movimientoMapper.toEntity(requestDTO);
        movimiento.setCuenta(cuenta);
        movimiento.setSaldo(nuevoSaldo);

        Movimiento saved = movimientoRepository.save(movimiento);

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return movimientoMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public MovimientoResponseDTO update(Long id, MovimientoRequestDTO requestDTO) {
        Movimiento movimiento = getMovimientoOrThrow(id);
        Cuenta cuenta = movimiento.getCuenta();

        BigDecimal saldoAntesDelMovimiento = movimiento.getSaldo().subtract(movimiento.getValor());
        BigDecimal nuevoSaldoMovimiento = calcularNuevoSaldo(saldoAntesDelMovimiento, requestDTO.getValor());

        BigDecimal saldoCuentaActual = cuenta.getSaldoInicial();
        BigDecimal delta = requestDTO.getValor().subtract(movimiento.getValor());
        BigDecimal nuevoSaldoCuenta = calcularNuevoSaldo(saldoCuentaActual, delta);

        movimientoMapper.updateEntityFromDTO(requestDTO, movimiento);
        movimiento.setSaldo(nuevoSaldoMovimiento);

        Movimiento updated = movimientoRepository.save(movimiento);

        cuenta.setSaldoInicial(nuevoSaldoCuenta);
        cuentaRepository.save(cuenta);

        return movimientoMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Movimiento movimiento = getMovimientoOrThrow(id);
        Cuenta cuenta = movimiento.getCuenta();

        BigDecimal nuevoSaldoCuenta = cuenta.getSaldoInicial().subtract(movimiento.getValor());
        if (nuevoSaldoCuenta.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException(SALDO_NO_DISPONIBLE);
        }

        cuenta.setSaldoInicial(nuevoSaldoCuenta);
        cuentaRepository.save(cuenta);
        movimientoRepository.delete(movimiento);
    }

    private Cuenta getCuentaByNumeroOrThrow(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número " + numeroCuenta));
    }

    private Movimiento getMovimientoOrThrow(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id " + id));
    }

    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, BigDecimal valorMovimiento) {
        BigDecimal nuevoSaldo = saldoActual.add(valorMovimiento);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException(SALDO_NO_DISPONIBLE);
        }
        return nuevoSaldo;
    }
}

