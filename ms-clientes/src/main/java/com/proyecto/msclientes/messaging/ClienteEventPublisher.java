package com.proyecto.msclientes.messaging;

import com.proyecto.msclientes.config.RabbitMQConfig;
import com.proyecto.msclientes.domain.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishClienteCreated(Cliente cliente) {
        publishEvent(cliente, ClienteEvent.EventType.CREATED);
    }

    public void publishClienteUpdated(Cliente cliente) {
        publishEvent(cliente, ClienteEvent.EventType.UPDATED);
    }

    public void publishClienteDeleted(Long clienteId, String nombre, String identificacion) {
        ClienteEvent event = ClienteEvent.builder()
                .clienteId(clienteId)
                .nombre(nombre)
                .identificacion(identificacion)
                .eventType(ClienteEvent.EventType.DELETED)
                .build();
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_CLIENTES,
                RabbitMQConfig.ROUTING_KEY_CLIENTE,
                event
        );
        
        log.info("ClienteDeleted event publicado para clienteId: {}", clienteId);
    }

    private void publishEvent(Cliente cliente, ClienteEvent.EventType eventType) {
        ClienteEvent event = ClienteEvent.builder()
                .clienteId(cliente.getClienteId())
                .nombre(cliente.getNombre())
                .identificacion(cliente.getIdentificacion())
                .estado(cliente.getEstado())
                .eventType(eventType)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_CLIENTES,
                RabbitMQConfig.ROUTING_KEY_CLIENTE,
                event
        );

        log.info("{} event publicado para clienteId: {}", eventType, cliente.getClienteId());
    }
}

