package com.proyecto.mscuentas.service;

import com.proyecto.mscuentas.dto.ClienteRefRequestDTO;
import com.proyecto.mscuentas.dto.ClienteRefResponseDTO;

import java.util.List;

public interface ClienteRefService {
    List<ClienteRefResponseDTO> findAll();

    ClienteRefResponseDTO findById(Long clienteId);

    ClienteRefResponseDTO create(ClienteRefRequestDTO requestDTO);

    ClienteRefResponseDTO update(Long clienteId, ClienteRefRequestDTO requestDTO);

    void delete(Long clienteId);
}

