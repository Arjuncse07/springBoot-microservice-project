package com.arjun.library_service.bookstore.library.catalog;

import com.arjun.library_service.bookstore.library.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CatalogClient {

    private final RestClient restClient;

    public CatalogClient(RestClient catalogRestClient) {
        this.restClient = catalogRestClient;
    }

    public CatalogProduct getProductByCode(String code) {
        try {
            return restClient
                    .get()
                    .uri("/api/products/{code}", code)
                    .retrieve()
                    .body(CatalogProduct.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw ProductNotFoundException.forCode(code);
        }
    }
}
