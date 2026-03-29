package com.proyecto.mscuentas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClienteRefTest {

    @Test
    void shouldSetUpdatedAtOnPersistHook() {
        ClienteRef clienteRef = ClienteRef.builder()
                .clienteId(1L)
                .nombre("Jose Lema")
                .identificacion("1234567890")
                .estado(true)
                .build();

        clienteRef.touchUpdatedAt();

        assertNotNull(clienteRef.getUpdatedAt());
    }
}

