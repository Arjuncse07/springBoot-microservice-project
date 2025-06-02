package com.arjun.order_service.bookstore.orders.domain.models;

public record OrderSummary(
        String orderNumber, OrderStatus status
) {
}
