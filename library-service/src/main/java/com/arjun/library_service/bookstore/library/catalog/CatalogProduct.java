package com.arjun.library_service.bookstore.library.catalog;

import java.math.BigDecimal;

public record CatalogProduct(String code, String name, String description, String imageUrl, BigDecimal price) {}
