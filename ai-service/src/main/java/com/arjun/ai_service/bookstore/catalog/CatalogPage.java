package com.arjun.ai_service.bookstore.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CatalogPage (
        List<CatalogProduct> data,
        long totalElement,
        int pageNumber,
        int totalPage,
        @JsonProperty("first") boolean first,
        @JsonProperty("last") boolean last,
        boolean hasNext,
        boolean hasPrevious){ }


