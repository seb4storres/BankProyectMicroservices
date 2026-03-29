package com.proyecto.mscuentas.mapper;

import com.proyecto.mscuentas.domain.Cuenta;
import com.proyecto.mscuentas.dto.CuentaRequestDTO;
import com.proyecto.mscuentas.dto.CuentaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toEntity(CuentaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Cuenta.builder()
                .numeroCuenta(dto.getNumeroCuenta())
                .tipoCuenta(dto.getTipoCuenta())
                .saldoInicial(dto.getSaldoInicial())
                .estado(dto.getEstado())
                .clienteId(dto.getClienteId())
                .build();
    }

    public CuentaResponseDTO toResponseDTO(Cuenta entity) {
        if (entity == null) {
            return null;
        }

        return CuentaResponseDTO.builder()
                .id(entity.getId())
                .numeroCuenta(entity.getNumeroCuenta())
                .tipoCuenta(entity.getTipoCuenta())
                .saldoInicial(entity.getSaldoInicial())
                .estado(entity.getEstado())
                .clienteId(entity.getClienteId())
                .build();
    }

    public void updateEntityFromDTO(CuentaRequestDTO dto, Cuenta entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getNumeroCuenta() != null) {
            entity.setNumeroCuenta(dto.getNumeroCuenta());
        }
        if (dto.getTipoCuenta() != null) {
            entity.setTipoCuenta(dto.getTipoCuenta());
        }
        if (dto.getSaldoInicial() != null) {
            entity.setSaldoInicial(dto.getSaldoInicial());
        }
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
        if (dto.getClienteId() != null) {
            entity.setClienteId(dto.getClienteId());
        }
    }
}

