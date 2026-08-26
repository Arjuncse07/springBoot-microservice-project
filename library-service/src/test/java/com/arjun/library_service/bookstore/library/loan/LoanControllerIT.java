package com.arjun.library_service.bookstore.library.loan;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.arjun.library_service.bookstore.library.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class LoanControllerIT extends AbstractIT {

    @Test
    void shouldCheckoutReturnAndRejectDoubleCheckout() {
        Long loanId = given().contentType(ContentType.JSON)
                .body("""
                        {
                          "userId": 42,
                          "barcode": "BC-P100"
                        }
                        """)
                .when()
                .post("/api/library/loans")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("userId", equalTo(42))
                .body("barcode", equalTo("BC-P100"))
                .body("status", equalTo("ACTIVE"))
                .extract()
                .path("id");

        given().when()
                .get("/api/library/copies/BC-P100")
                .then()
                .statusCode(200)
                .body("status", equalTo("LOANED"));

        given().contentType(ContentType.JSON)
                .body("""
                        {
                          "userId": 99,
                          "barcode": "BC-P100"
                        }
                        """)
                .when()
                .post("/api/library/loans")
                .then()
                .statusCode(409);

        given().when().post("/api/library/loans/{id}/return", loanId).then().statusCode(200).body("status", equalTo("RETURNED"));

        given().when()
                .get("/api/library/copies/BC-P100")
                .then()
                .statusCode(200)
                .body("status", equalTo("AVAILABLE"));

        given().queryParam("userId", 42)
                .queryParam("status", "RETURNED")
                .when()
                .get("/api/library/loans")
                .then()
                .statusCode(200)
                .body("id[0]", equalTo(loanId.intValue()));
    }

    @Test
    void shouldListOverdueLoans() {
        given().contentType(ContentType.JSON)
                .body("""
                        {
                          "userId": 7,
                          "barcode": "BC-P101"
                        }
                        """)
                .when()
                .post("/api/library/loans")
                .then()
                .statusCode(201);

        given().when().get("/api/library/loans/overdue").then().statusCode(200);
    }

    @Test
    void shouldRenewAndRejectWhenMaxExceeded() {
        Long loanId = given().contentType(ContentType.JSON)
                .body("""
                        {
                          "userId": 42,
                          "barcode": "BC-P102"
                        }
                        """)
                .when()
                .post("/api/library/loans")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given().when().post("/api/library/loans/{id}/renew", loanId).then().statusCode(200);
        given().when().post("/api/library/loans/{id}/renew", loanId).then().statusCode(200);
        given().when().post("/api/library/loans/{id}/renew", loanId).then().statusCode(403);
    }
}
