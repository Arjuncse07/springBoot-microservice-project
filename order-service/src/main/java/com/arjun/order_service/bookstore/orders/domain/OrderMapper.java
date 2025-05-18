package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderRequest;
import com.arjun.order_service.bookstore.orders.domain.models.OrderItem;
import com.arjun.order_service.bookstore.orders.domain.models.OrderStatus;
import org.hibernate.query.Order;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderMapper {

    static OrderEntity convertToEntity(CreateOrderRequest request){
        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(request.customer());
        newOrder.setDeliveryAddress(request.deliveryAddress());

        Set<OrderItemEntity> orderItemEntities = new HashSet<>();
        for (OrderItem item: request.items()){
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItemEntities.add(orderItem);
        }
        newOrder.setItems(orderItemEntities);
        return newOrder;
    }





}
