package com.arjun.user_service.bookstore.users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class RegisterUserIT extends AbstractIT {

    @Test
    void shouldRegisterUserSuccessfully() {
        given().contentType(ContentType.JSON)
                .body("""
                        {
                          "username": "jane.doe",
                          "email": "jane.doe@example.com",
                          "password": "SecureP@ss1",
                          "firstName": "Jane",
                          "lastName": "Doe",
                          "phone": "+1-555-0100"
                        }
                        """)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201)
                .body("accessToken", not(blankOrNullString()))
                .body("tokenType", equalTo("Bearer"))
                .body("expiresIn", greaterThan(0))
                .body("user.username", equalTo("jane.doe"))
                .body("user.email", equalTo("jane.doe@example.com"))
                .body("user.firstName", equalTo("Jane"))
                .body("user.lastName", equalTo("Doe"))
                .body("user.role", equalTo("USER"));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        String payload =
                """
                        {
                          "username": "john.doe",
                          "email": "duplicate@example.com",
                          "password": "SecureP@ss1",
                          "firstName": "John",
                          "lastName": "Doe"
                        }
                        """;

        given().contentType(ContentType.JSON)
                .body(payload)
                .post("/api/auth/register")
                .then()
                .statusCode(201);

        given().contentType(ContentType.JSON)
                .body(payload.replace("john.doe", "johnny.doe"))
                .post("/api/auth/register")
                .then()
                .statusCode(409)
                .body("title", equalTo("User Already Exists"));
    }

    @Test
    void shouldRejectInvalidRegistrationPayload() {
        given().contentType(ContentType.JSON)
                .body("""
                        {
                          "username": "ab",
                          "email": "not-an-email",
                          "password": "short",
                          "firstName": "",
                          "lastName": ""
                        }
                        """)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(400)
                .body("title", equalTo("Bad Request"))
                .body("errors", hasSize(greaterThan(0)));
    }
}
