package com.arjun.order_service.bookstore.orders.domain;

public class OrdersNotFoundException extends RuntimeException {

    public OrdersNotFoundException(String message){
        super(message);
    }

    public static OrdersNotFoundException forOrderNumber(String orderNumber){
        return new OrdersNotFoundException("Order with number : "+orderNumber+" not found" );
    }

}
