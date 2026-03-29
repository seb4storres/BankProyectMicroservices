package com.proyecto.mscuentas.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEvent {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
    private String eventType;
}

