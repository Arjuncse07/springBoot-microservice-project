package com.arjun.library_service.bookstore.library.copy;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.arjun.library_service.bookstore.library.AbstractIT;
import com.arjun.library_service.bookstore.library.catalog.CatalogClient;
import com.arjun.library_service.bookstore.library.catalog.CatalogProduct;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CopyControllerIT extends AbstractIT {

    @MockitoBean
    private CatalogClient catalogClient;

    @Test
    void shouldCreateAndGetCopy() {
        when(catalogClient.getProductByCode(eq("P105")))
                .thenReturn(new CatalogProduct("P105", "The Giving Tree", null, null, new BigDecimal("32.0")));

        given().contentType(ContentType.JSON)
                .body("""
                        {
                          "barcode": "BC-P105",
                          "productCode": "P105",
                          "organizationId": 1
                        }
                        """)
                .when()
                .post("/api/library/copies")
                .then()
                .statusCode(201)
                .body("barcode", equalTo("BC-P105"))
                .body("productCode", equalTo("P105"))
                .body("status", equalTo("AVAILABLE"));

        given().when()
                .get("/api/library/copies/BC-P105")
                .then()
                .statusCode(200)
                .body("barcode", equalTo("BC-P105"));
    }

    @Test
    void shouldRejectDuplicateBarcode() {
        when(catalogClient.getProductByCode(eq("P106")))
                .thenReturn(new CatalogProduct("P106", "The Da Vinci Code", null, null, new BigDecimal("14.5")));

        String body =
                """
                {
                  "barcode": "BC-P106",
                  "productCode": "P106",
                  "organizationId": 1
                }
                """;

        given().contentType(ContentType.JSON).body(body).when().post("/api/library/copies").then().statusCode(201);

        given().contentType(ContentType.JSON).body(body).when().post("/api/library/copies").then().statusCode(409);
    }

    @Test
    void shouldFilterCopiesByStatus() {
        given().queryParam("status", "AVAILABLE")
                .when()
                .get("/api/library/copies")
                .then()
                .statusCode(200)
                .body("barcode", hasItem("BC-P100"));
    }
}
