package com.proyecto.mscuentas.mapper;

import com.proyecto.mscuentas.domain.Movimiento;
import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.dto.MovimientoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public Movimiento toEntity(MovimientoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Movimiento.builder()
                .tipoMovimiento(dto.getTipoMovimiento())
                .valor(dto.getValor())
                .build();
    }

    public MovimientoResponseDTO toResponseDTO(Movimiento entity) {
        if (entity == null) {
            return null;
        }

        return MovimientoResponseDTO.builder()
                .id(entity.getId())
                .fecha(entity.getFecha())
                .tipoMovimiento(entity.getTipoMovimiento())
                .valor(entity.getValor())
                .saldo(entity.getSaldo())
                .numeroCuenta(entity.getCuenta() != null ? entity.getCuenta().getNumeroCuenta() : null)
                .build();
    }

    public void updateEntityFromDTO(MovimientoRequestDTO dto, Movimiento entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getTipoMovimiento() != null) {
            entity.setTipoMovimiento(dto.getTipoMovimiento());
        }
        if (dto.getValor() != null) {
            entity.setValor(dto.getValor());
        }
    }
}

