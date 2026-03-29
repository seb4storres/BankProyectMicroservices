package com.proyecto.mscuentas.service;

import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.dto.MovimientoResponseDTO;

import java.util.List;

public interface MovimientoService {
    List<MovimientoResponseDTO> findAll();

    MovimientoResponseDTO findById(Long id);

    MovimientoResponseDTO create(MovimientoRequestDTO requestDTO);

    MovimientoResponseDTO update(Long id, MovimientoRequestDTO requestDTO);

    void delete(Long id);
}

