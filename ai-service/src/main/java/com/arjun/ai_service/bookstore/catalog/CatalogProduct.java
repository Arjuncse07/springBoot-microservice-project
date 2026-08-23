package com.arjun.ai_service.bookstore.catalog;

import java.math.BigDecimal;

public record CatalogProduct(String code,
                            String name,
                            String description,
                            String imageUrl, BigDecimal price) { }
