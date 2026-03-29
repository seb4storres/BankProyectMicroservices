package com.proyecto.mscuentas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_CLIENTES = "exchange.clientes";
    public static final String QUEUE_CLIENTES = "queue.clientes";
    public static final String ROUTING_KEY_CLIENTE_EVENT = "cliente.event";

    @Bean
    public DirectExchange clientesExchange() {
        return new DirectExchange(EXCHANGE_CLIENTES, true, false);
    }

    @Bean
    public Queue clientesQueue() {
        return new Queue(QUEUE_CLIENTES, true);
    }

    @Bean
    public Binding clienteEventBinding(Queue clientesQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(clientesQueue).to(clientesExchange).with(ROUTING_KEY_CLIENTE_EVENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

