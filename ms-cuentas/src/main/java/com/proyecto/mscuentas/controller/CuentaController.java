package com.proyecto.mscuentas.controller;

import com.proyecto.mscuentas.dto.CuentaRequestDTO;
import com.proyecto.mscuentas.dto.CuentaResponseDTO;
import com.proyecto.mscuentas.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> findAll() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> create(@Valid @RequestBody CuentaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CuentaRequestDTO requestDTO) {
        return ResponseEntity.ok(cuentaService.update(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> patch(@PathVariable Long id, @RequestBody CuentaRequestDTO requestDTO) {
        return ResponseEntity.ok(cuentaService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

