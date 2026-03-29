package com.proyecto.mscuentas.messaging;

import com.proyecto.mscuentas.config.RabbitMQConfig;
import com.proyecto.mscuentas.domain.ClienteRef;
import com.proyecto.mscuentas.repository.ClienteRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ClienteEventConsumer {

    private final ClienteRefRepository clienteRefRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CLIENTES)
    public void onClienteEvent(ClienteEvent event) {
        if (event == null || event.getClienteId() == null || event.getEventType() == null) {
            return;
        }

        String eventType = event.getEventType().trim().toUpperCase();
        switch (eventType) {
            case "CREATED", "UPDATED" -> upsert(event);
            case "DELETED" -> markAsInactive(event.getClienteId());
            default -> {
                // Ignora eventos desconocidos para mantener compatibilidad hacia adelante.
            }
        }
    }

    private void upsert(ClienteEvent event) {
        ClienteRef cliente = clienteRefRepository.findById(event.getClienteId())
                .orElseGet(ClienteRef::new);

        cliente.setClienteId(event.getClienteId());
        cliente.setNombre(Objects.requireNonNullElse(event.getNombre(), "N/D"));
        cliente.setIdentificacion(Objects.requireNonNullElse(event.getIdentificacion(), "N/D"));
        cliente.setEstado(Boolean.TRUE.equals(event.getEstado()));

        clienteRefRepository.save(cliente);
    }

    private void markAsInactive(Long clienteId) {
        clienteRefRepository.findById(clienteId).ifPresent(cliente -> {
            cliente.setEstado(Boolean.FALSE);
            clienteRefRepository.save(cliente);
        });
    }
}

