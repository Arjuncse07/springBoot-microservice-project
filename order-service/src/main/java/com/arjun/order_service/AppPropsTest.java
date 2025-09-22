package com.arjun.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppPropsTest {

    @Autowired
    public ApplicationProperties properties;


    public void display(){
        System.out.println(properties.catalogServiceUrl());
        System.out.println(properties.orderEventsExchange());
        System.out.println(properties.newOrdersQueue());
        System.out.println(properties.deliveredOrdersQueue());
        System.out.println(properties.errorOrdersQueue());
    }

}
