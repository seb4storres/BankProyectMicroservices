package com.proyecto.msclientes.service;

import com.proyecto.msclientes.dto.ClienteRequestDTO;
import com.proyecto.msclientes.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteResponseDTO> findAll();

    ClienteResponseDTO findById(Long id);

    ClienteResponseDTO create(ClienteRequestDTO dto);

    ClienteResponseDTO update(Long id, ClienteRequestDTO dto);

    void delete(Long id);
}