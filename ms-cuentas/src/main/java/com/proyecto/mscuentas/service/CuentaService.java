package com.proyecto.mscuentas.service;

import com.proyecto.mscuentas.dto.CuentaRequestDTO;
import com.proyecto.mscuentas.dto.CuentaResponseDTO;

import java.util.List;

public interface CuentaService {
    List<CuentaResponseDTO> findAll();

    CuentaResponseDTO findById(Long id);

    CuentaResponseDTO create(CuentaRequestDTO requestDTO);

    CuentaResponseDTO update(Long id, CuentaRequestDTO requestDTO);

    void delete(Long id);
}

