package com.proyecto.msclientes.mapper;

import com.proyecto.msclientes.domain.Cliente;
import com.proyecto.msclientes.dto.ClienteRequestDTO;
import com.proyecto.msclientes.dto.ClienteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponseDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }

        return ClienteResponseDTO.builder()
                .clienteId(entity.getClienteId())
                .nombre(entity.getNombre())
                .genero(entity.getGenero())
                .edad(entity.getEdad())
                .identificacion(entity.getIdentificacion())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .estado(entity.getEstado())
                .build();
    }

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado());
        return cliente;
    }

    public void updateEntityFromDTO(ClienteRequestDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
        if (dto.getGenero() != null) {
            entity.setGenero(dto.getGenero());
        }
        if (dto.getEdad() != null) {
            entity.setEdad(dto.getEdad());
        }
        if (dto.getIdentificacion() != null) {
            entity.setIdentificacion(dto.getIdentificacion());
        }
        if (dto.getDireccion() != null) {
            entity.setDireccion(dto.getDireccion());
        }
        if (dto.getTelefono() != null) {
            entity.setTelefono(dto.getTelefono());
        }
        if (dto.getContrasena() != null) {
            entity.setContrasena(dto.getContrasena());
        }
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
    }
}

