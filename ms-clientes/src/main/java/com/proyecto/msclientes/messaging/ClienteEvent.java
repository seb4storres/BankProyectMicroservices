package com.proyecto.msclientes.messaging;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteEvent implements Serializable {

    public enum EventType { CREATED, UPDATED, DELETED }
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
    private EventType eventType;
}