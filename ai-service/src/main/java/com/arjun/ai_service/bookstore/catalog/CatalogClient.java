package com.arjun.ai_service.bookstore.catalog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogClient {

    private final RestClient restClient;


    public CatalogClient(
            RestClient.Builder builder,
            @Value("${catalog.service-url}") String catalogServiceUrl){
        this.restClient = builder.baseUrl(catalogServiceUrl).build();
    }


    public List<CatalogProduct> fetchAllProducts(){
        List<CatalogProduct> listOfCatalogProduct = new ArrayList<>();
        int page = 1;
        while(true){
            int finalPage = page;
            CatalogPage result = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/api/products").queryParam("page", finalPage).build())
                    .retrieve()
                    .body(CatalogPage.class);

            if (result == null || result.data() == null || result.data().isEmpty()){
                break;
            }

            listOfCatalogProduct.addAll(result.data());
            if(result.last() || result.hasNext()){
                break;
            }
            page++;
        }
        return listOfCatalogProduct;
    }





}
