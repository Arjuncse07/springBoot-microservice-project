package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.ApplicationProperties;
import com.arjun.order_service.bookstore.orders.domain.models.OrderCancelledEvent;
import com.arjun.order_service.bookstore.orders.domain.models.OrderCreatedEvent;
import com.arjun.order_service.bookstore.orders.domain.models.OrderDeliveredEvent;
import com.arjun.order_service.bookstore.orders.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

     OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    /* Here [ Routing key :: Queue Name ]     */
    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }


    public void publish(OrderCreatedEvent event){
        this.send(properties.newOrdersQueue(),event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }







}
