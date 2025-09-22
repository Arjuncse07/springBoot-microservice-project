package com.arjun.bookstore.notifications.domain.models;

import com.arjun.order_service.bookstore.orders.domain.models.Customer;
import com.arjun.order_service.bookstore.orders.domain.models.OrderItem;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {
}
