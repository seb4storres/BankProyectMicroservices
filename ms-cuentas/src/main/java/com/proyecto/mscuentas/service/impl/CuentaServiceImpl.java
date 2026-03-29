package com.proyecto.mscuentas.service.impl;

import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.domain.Cuenta;
import com.proyecto.mscuentas.dto.CuentaRequestDTO;
import com.proyecto.mscuentas.dto.CuentaResponseDTO;
import com.proyecto.mscuentas.exception.DuplicateResourceException;
import com.proyecto.mscuentas.exception.ResourceNotFoundException;
import com.proyecto.mscuentas.mapper.CuentaMapper;
import com.proyecto.mscuentas.repository.ClienteRefRepository;
import com.proyecto.mscuentas.repository.CuentaRepository;
import com.proyecto.mscuentas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRefRepository clienteRefRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> findAll() {
        return cuentaRepository.findAll().stream()
                .map(cuentaMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponseDTO findById(Long id) {
        Cuenta cuenta = getCuentaOrThrow(id);
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    @Transactional
    public CuentaResponseDTO create(CuentaRequestDTO requestDTO) {
        validateNumeroCuentaForCreate(requestDTO.getNumeroCuenta());
        validateClienteActivo(requestDTO.getClienteId());

        Cuenta cuenta = cuentaMapper.toEntity(requestDTO);
        Cuenta saved = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public CuentaResponseDTO update(Long id, CuentaRequestDTO requestDTO) {
        Cuenta cuenta = getCuentaOrThrow(id);

        if (requestDTO.getNumeroCuenta() != null
                && cuentaRepository.existsByNumeroCuentaAndIdNot(requestDTO.getNumeroCuenta(), id)) {
            throw new DuplicateResourceException("Ya existe una cuenta con el número " + requestDTO.getNumeroCuenta());
        }

        Long clienteId = requestDTO.getClienteId() != null ? requestDTO.getClienteId() : cuenta.getClienteId();
        validateClienteActivo(clienteId);

        cuentaMapper.updateEntityFromDTO(requestDTO, cuenta);
        return cuentaMapper.toResponseDTO(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cuenta cuenta = getCuentaOrThrow(id);
        cuentaRepository.delete(cuenta);
    }

    private Cuenta getCuentaOrThrow(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id " + id));
    }

    private void validateNumeroCuentaForCreate(String numeroCuenta) {
        if (cuentaRepository.existsByNumeroCuenta(numeroCuenta)) {
            throw new DuplicateResourceException("Ya existe una cuenta con el número " + numeroCuenta);
        }
    }

    private void validateClienteActivo(Long clienteId) {
        ClienteRef cliente = clienteRefRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + clienteId));

        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new ResourceNotFoundException("Cliente inactivo con id " + clienteId);
        }
    }
}

