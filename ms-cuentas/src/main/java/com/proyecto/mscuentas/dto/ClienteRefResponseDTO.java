package com.proyecto.mscuentas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRefResponseDTO {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
    private LocalDateTime updatedAt;
}

