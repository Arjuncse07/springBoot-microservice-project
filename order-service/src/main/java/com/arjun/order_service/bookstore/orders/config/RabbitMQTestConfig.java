package com.arjun.order_service.bookstore.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTestConfig {

    @Value("${orders.order-events-exchange}")
    private String exchange;

    private String queue;

    private String routingKey;

}
