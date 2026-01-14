package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.domain.TypeMethod;
import org.acme.domain.TypePayment;
import org.acme.dto.PaymentRequest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PaymentResourceTest {

    @Test
    public void testCreateAndGetPayment() {
        PaymentRequest request = new PaymentRequest(
                "REF-TEST-001",
                100L,
                50.50,
                TypePayment.USD,
                TypeMethod.CARD
        );

        // 1. Crear el pago (POST)
        Integer id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/payments")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", is("PENDING"))
                .extract()
                .path("id");

        // 2. Obtener el pago creado (GET)
        given()
                .pathParam("id", id)
                .when()
                .get("/api/payments/{id}")
                .then()
                .statusCode(200)
                .body("reference", is("REF-TEST-001"))
                .body("amount", is(50.50F));
    }
}
