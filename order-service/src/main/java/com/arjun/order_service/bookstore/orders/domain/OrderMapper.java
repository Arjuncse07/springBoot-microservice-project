package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.CreateOrderRequest;
import com.arjun.order_service.bookstore.orders.domain.models.OrderDTO;
import com.arjun.order_service.bookstore.orders.domain.models.OrderItem;
import com.arjun.order_service.bookstore.orders.domain.models.OrderStatus;
import org.hibernate.query.Order;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    static OrderDTO convertToDTO(OrderEntity order){
       Set<OrderItem> orderItems = order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(),item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

       return new OrderDTO(
        order.getOrderNumber(),
        order.getUserName(),
        orderItems,
        order.getCustomer(),
        order.getDeliveryAddress(),
        order.getStatus(),
        order.getComments(),
        order.getCreatedAt()
       );
    }





}
