package com.arjun.bookstore.notifications.events;

import com.arjun.bookstore.notifications.domain.models.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {


    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event){
        System.out.println("Order Created Event : "+event);
    }



}
