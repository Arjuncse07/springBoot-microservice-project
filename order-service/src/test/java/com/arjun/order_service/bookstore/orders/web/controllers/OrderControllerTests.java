package com.arjun.order_service.bookstore.orders.web.controllers;

import com.arjun.order_service.bookstore.orders.AbstractIT;
import io.restassured.http.ContentType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;

import java.math.BigDecimal;

@Sql("test-orders.sql")
class OrderControllerTests extends AbstractIT {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));
            var payload =
                    """
                              {
                                  "customer": {
                                      "name": "Siva",
                                      "email": "siva@gmail.com",
                                      "phone": "999999999"
                                  },
                                  "deliveryAddress": {
                                      "addressLine1": "HNO 123",
                                      "addressLine2": "Kukatpally",
                                      "city": "Hyderabad",
                                      "state": "Telangana",
                                      "zipCode": "500072",
                                      "country": "India"
                                  },
                                  "items": [
                                      {
                                          "code": "P100",
                                          "name": "Product 1",
                                          "price": 25.50,
                                          "quantity": 1
                                      }
                                  ]
                              }
                            """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());

        }
    }

    /*  working on test data later  */
    /*@Test
    void shouldReturnBadRequestWhenMandatoryDataIsMissing(){
        var payload = "ae";
        given().contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }*/


}
