package com.arjun.order_service.bookstore.orders.clients.catalog;

import com.arjun.order_service.ApplicationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class CatalogServiceClientConfig {

    @Bean
    RestClient restClient(ApplicationProperties applicationProperties){
        return RestClient.builder()
                .baseUrl(applicationProperties.catalogServiceUrl())
                .requestFactory(ClientHttpRequestFactories
                        .get(ClientHttpRequestFactorySettings.DEFAULTS
                                .withConnectTimeout(Duration.ofSeconds(5))
                                .withReadTimeout(Duration.ofSeconds(5)))).build();
    }

}
