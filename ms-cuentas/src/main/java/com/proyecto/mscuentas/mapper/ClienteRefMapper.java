package com.proyecto.mscuentas.mapper;

import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.dto.ClienteRefRequestDTO;
import com.proyecto.mscuentas.dto.ClienteRefResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ClienteRefMapper {

    public ClienteRef toEntity(ClienteRefRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return ClienteRef.builder()
                .clienteId(dto.getClienteId())
                .nombre(dto.getNombre())
                .identificacion(dto.getIdentificacion())
                .estado(dto.getEstado())
                .build();
    }

    public ClienteRefResponseDTO toResponseDTO(ClienteRef entity) {
        if (entity == null) {
            return null;
        }

        return ClienteRefResponseDTO.builder()
                .clienteId(entity.getClienteId())
                .nombre(entity.getNombre())
                .identificacion(entity.getIdentificacion())
                .estado(entity.getEstado())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(ClienteRefRequestDTO dto, ClienteRef entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
        if (dto.getIdentificacion() != null) {
            entity.setIdentificacion(dto.getIdentificacion());
        }
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
    }
}

