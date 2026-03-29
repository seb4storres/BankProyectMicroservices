package com.proyecto.mscuentas.controller;

import com.proyecto.mscuentas.dto.ClienteRefRequestDTO;
import com.proyecto.mscuentas.dto.ClienteRefResponseDTO;
import com.proyecto.mscuentas.service.ClienteRefService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRefService clienteRefService;

    @GetMapping
    public ResponseEntity<List<ClienteRefResponseDTO>> findAll() {
        return ResponseEntity.ok(clienteRefService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteRefResponseDTO> findById(@PathVariable("id") Long clienteId) {
        return ResponseEntity.ok(clienteRefService.findById(clienteId));
    }

    @PostMapping
    public ResponseEntity<ClienteRefResponseDTO> create(@Valid @RequestBody ClienteRefRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRefService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteRefResponseDTO> update(@PathVariable("id") Long clienteId,
                                                        @RequestBody ClienteRefRequestDTO requestDTO) {
        return ResponseEntity.ok(clienteRefService.update(clienteId, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteRefResponseDTO> patch(@PathVariable("id") Long clienteId,
                                                       @RequestBody ClienteRefRequestDTO requestDTO) {
        return ResponseEntity.ok(clienteRefService.update(clienteId, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long clienteId) {
        clienteRefService.delete(clienteId);
        return ResponseEntity.noContent().build();
    }
}

