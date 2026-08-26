package com.arjun.library_service.bookstore.library.card;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import com.arjun.library_service.bookstore.library.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class CardControllerIT extends AbstractIT {

    @Test
    void shouldIssueBlockAndCheckStatus() {
        String cardId = given().contentType(ContentType.JSON)
                .body("""
                        {
                          "userId": 1,
                          "cardType": "PERMANENT",
                          "organizationId": 1,
                          "securityDeposit": 500.00
                        }
                        """)
                .when()
                .post("/api/library/cards")
                .then()
                .statusCode(201)
                .body("cardId", startsWith("CARD-"))
                .body("status", equalTo("ACTIVE"))
                .body("qrCodeData", notNullValue())
                .body("qrCodeImageBase64", notNullValue())
                .extract()
                .path("cardId");

        given().when().get("/api/library/cards/{cardId}", cardId).then().statusCode(200);

        given().when()
                .patch("/api/library/cards/{cardId}/block", cardId)
                .then()
                .statusCode(200)
                .body("status", equalTo("BLOCKED"));

        given().when()
                .get("/api/library/cards/{cardId}/status", cardId)
                .then()
                .statusCode(200)
                .body("status", equalTo("BLOCKED"))
                .body("canEnter", equalTo(false));
    }
}
