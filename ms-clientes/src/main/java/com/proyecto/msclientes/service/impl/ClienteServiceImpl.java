package com.proyecto.msclientes.service.impl;

import com.proyecto.msclientes.domain.Cliente;
import com.proyecto.msclientes.dto.ClienteRequestDTO;
import com.proyecto.msclientes.dto.ClienteResponseDTO;
import com.proyecto.msclientes.exception.DuplicateResourceException;
import com.proyecto.msclientes.exception.ResourceNotFoundException;
import com.proyecto.msclientes.mapper.ClienteMapper;
import com.proyecto.msclientes.messaging.ClienteEventPublisher;
import com.proyecto.msclientes.repository.ClienteRepository;
import com.proyecto.msclientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteEventPublisher eventPublisher;

    @Override
    public List<ClienteResponseDTO> findAll() {
        log.info("Consultando todos los clientes");
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        log.info("Consultando cliente con id: {}", id);
        return clienteRepository.findById(id)
                .map(clienteMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO dto) {
        log.info("Creando cliente con identificación: {}", dto.getIdentificacion());

        if (clienteRepository.existsByIdentificacion(dto.getIdentificacion())) {
            throw new DuplicateResourceException(
                    "Ya existe un cliente con la identificación: " + dto.getIdentificacion());
        }

        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente saved = clienteRepository.save(cliente);

        eventPublisher.publishClienteCreated(saved);

        log.info("Cliente creado con id: {}", saved.getClienteId());
        return clienteMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto) {
        log.info("Actualizando cliente con id: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        if (dto.getIdentificacion() != null
                && clienteRepository.existsByIdentificacionAndClienteIdNot(dto.getIdentificacion(), id)) {
            throw new DuplicateResourceException(
                    "Ya existe otro cliente con la identificación: " + dto.getIdentificacion());
        }

        clienteMapper.updateEntityFromDTO(dto, cliente);
        Cliente updated = clienteRepository.save(cliente);

        eventPublisher.publishClienteUpdated(updated);

        log.info("Cliente actualizado con id: {}", updated.getClienteId());
        return clienteMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        eventPublisher.publishClienteDeleted(cliente.getClienteId(), cliente.getNombre(), cliente.getIdentificacion());
        clienteRepository.delete(cliente);
        log.info("Cliente eliminado con id: {}", id);
    }
}