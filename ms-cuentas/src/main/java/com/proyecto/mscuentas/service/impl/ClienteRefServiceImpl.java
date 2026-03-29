package com.proyecto.mscuentas.service.impl;

import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.dto.ClienteRefRequestDTO;
import com.proyecto.mscuentas.dto.ClienteRefResponseDTO;
import com.proyecto.mscuentas.exception.DuplicateResourceException;
import com.proyecto.mscuentas.exception.ResourceNotFoundException;
import com.proyecto.mscuentas.mapper.ClienteRefMapper;
import com.proyecto.mscuentas.repository.ClienteRefRepository;
import com.proyecto.mscuentas.service.ClienteRefService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteRefServiceImpl implements ClienteRefService {

    private final ClienteRefRepository clienteRefRepository;
    private final ClienteRefMapper clienteRefMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteRefResponseDTO> findAll() {
        return clienteRefRepository.findAll().stream()
                .map(clienteRefMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteRefResponseDTO findById(Long clienteId) {
        return clienteRefMapper.toResponseDTO(getByIdOrThrow(clienteId));
    }

    @Override
    @Transactional
    public ClienteRefResponseDTO create(ClienteRefRequestDTO requestDTO) {
        if (clienteRefRepository.existsById(requestDTO.getClienteId())) {
            throw new DuplicateResourceException("Ya existe un cliente con id " + requestDTO.getClienteId());
        }
        if (identificacionAlreadyExists(requestDTO.getIdentificacion(), null)) {
            throw new DuplicateResourceException("Ya existe un cliente con identificacion " + requestDTO.getIdentificacion());
        }

        ClienteRef saved = clienteRefRepository.save(clienteRefMapper.toEntity(requestDTO));
        return clienteRefMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ClienteRefResponseDTO update(Long clienteId, ClienteRefRequestDTO requestDTO) {
        ClienteRef current = getByIdOrThrow(clienteId);

        if (requestDTO.getIdentificacion() != null
                && identificacionAlreadyExists(requestDTO.getIdentificacion(), clienteId)) {
            throw new DuplicateResourceException("Ya existe un cliente con identificacion " + requestDTO.getIdentificacion());
        }

        clienteRefMapper.updateEntityFromDTO(requestDTO, current);
        return clienteRefMapper.toResponseDTO(clienteRefRepository.save(current));
    }

    @Override
    @Transactional
    public void delete(Long clienteId) {
        ClienteRef current = getByIdOrThrow(clienteId);
        current.setEstado(Boolean.FALSE);
        clienteRefRepository.save(current);
    }

    private ClienteRef getByIdOrThrow(Long clienteId) {
        return clienteRefRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + clienteId));
    }

    private boolean identificacionAlreadyExists(String identificacion, Long clienteIdToExclude) {
        return clienteRefRepository.findAll().stream()
                .anyMatch(cliente -> cliente.getIdentificacion().equalsIgnoreCase(identificacion)
                        && (clienteIdToExclude == null || !java.util.Objects.equals(cliente.getClienteId(), clienteIdToExclude)));
    }
}
