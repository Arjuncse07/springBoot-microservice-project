package com.arjun.order_service.bookstore.orders.clients.catalog;

import java.math.BigDecimal;

public record Product(
        String code, String name, String description, String imageUrl, BigDecimal price
) {
}
