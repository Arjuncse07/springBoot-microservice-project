package com.arjun.order_service.bookstore.orders;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class AbstractIT {

    @LocalServerPort
    int port;

    // RestAssured For Testing Api Endpoints

    @BeforeEach
    void setup(){
        RestAssured.port = port;
    }

}
