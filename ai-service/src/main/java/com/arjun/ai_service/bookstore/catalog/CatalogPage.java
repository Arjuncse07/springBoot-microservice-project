package com.arjun.ai_service.bookstore.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CatalogPage (
        List<CatalogProduct> data,
        @JsonProperty("totalElements") long totalElements,
        @JsonProperty("pageNumber") int pageNumber,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("isFirst") boolean first,
        @JsonProperty("isLast") boolean last,
        @JsonProperty("hasNext") boolean hasNext,
        @JsonProperty("hasPrevious") boolean hasPrevious
){ }


