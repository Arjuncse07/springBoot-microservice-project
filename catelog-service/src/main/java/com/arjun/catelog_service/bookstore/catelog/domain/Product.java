package com.arjun.catelog_service.bookstore.catelog.domain;

import java.math.BigDecimal;

public record Product(
        String code,
        String name,
        String description,
        String imageUrl,
        BigDecimal price) {
}
