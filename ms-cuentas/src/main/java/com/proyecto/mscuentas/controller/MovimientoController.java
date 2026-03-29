package com.proyecto.mscuentas.controller;

import com.proyecto.mscuentas.dto.MovimientoRequestDTO;
import com.proyecto.mscuentas.dto.MovimientoResponseDTO;
import com.proyecto.mscuentas.service.MovimientoService;
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
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> findAll() {
        return ResponseEntity.ok(movimientoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> create(@Valid @RequestBody MovimientoRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> update(@PathVariable Long id,
                                                        @Valid @RequestBody MovimientoRequestDTO requestDTO) {
        return ResponseEntity.ok(movimientoService.update(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> patch(@PathVariable Long id,
                                                       @RequestBody MovimientoRequestDTO requestDTO) {
        return ResponseEntity.ok(movimientoService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

