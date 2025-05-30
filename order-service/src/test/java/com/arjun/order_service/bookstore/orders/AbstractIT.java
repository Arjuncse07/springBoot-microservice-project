package com.arjun.order_service.bookstore.orders;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.TestcontainersConfiguration;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class AbstractIT {

    @LocalServerPort
    int port;

    // RestAssured For Testing Api Endpoints

    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");


    @BeforeAll
    static void beforeAll() {
        wireMockContainer.start();
        WireMock.configureFor(wireMockContainer.getHost(), wireMockContainer.getPort());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wireMockContainer::getBaseUrl);
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }


    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/api/products/" + code))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                        .withStatus(200)
                        .withBody(
                                """
                                        {
                                         "code": "%s",
                                         "name": "%s",
                                         "price": %f       
                                        }        
                                        """
                                        .formatted(code, name, price.doubleValue())
                        )
                )
        );
    }







}
