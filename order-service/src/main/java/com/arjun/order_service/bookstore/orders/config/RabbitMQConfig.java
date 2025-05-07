package com.arjun.order_service.bookstore.orders.config;

import com.arjun.order_service.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    private final ApplicationProperties properties;


    RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.orderEventsExchange());
    }

    /*@Bean
    public Queue createQueue(String queueName) {
        if (queueName == null) {
            throw new IllegalArgumentException("Queue name cannot be null");
        }
        return QueueBuilder.durable(queueName).build();
    }*/


    @Bean
    Queue newOrdersQueue() {
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrdersQueueBinding() {
        return BindingBuilder.bind(newOrdersQueue())
                .to(exchange())
                .with(properties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrderQueue() {
        String queueName = properties.deliveredOrdersQueue();
        if (queueName == null) {
            throw new IllegalArgumentException("deliveredOrderQueue name cannot be null");
        }
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    Binding deliveredOrdersQueueBinding() {
        return BindingBuilder.bind(deliveredOrderQueue())
                .to(exchange())
                .with(properties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrdersQueue() {
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding cancelledOrdersQueueBinding() {
        return BindingBuilder.bind(cancelledOrdersQueue())
                .to(exchange())
                .with(properties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrdersQueue() {
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrdersQueueBinding() {
        return BindingBuilder.bind(errorOrdersQueue())
                .to(exchange())
                .with(properties.errorOrdersQueue());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }


}
