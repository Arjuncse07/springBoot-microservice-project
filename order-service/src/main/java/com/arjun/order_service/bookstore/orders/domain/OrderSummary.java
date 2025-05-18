package com.arjun.order_service.bookstore.orders.domain;

import com.arjun.order_service.bookstore.orders.domain.models.OrderStatus;

public record OrderSummary(
        String orderNumber, OrderStatus status
) {
}
