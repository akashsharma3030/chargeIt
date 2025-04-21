package com.chargeIt.driver.register

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test


@QuarkusTest
class ChargingSessionControllerTest {
    @Test
    fun testStartChargingSession() {
        val requestBody = """
            {
                "driverToken": "123451111111111111111111111111111111",
                "stationId": "67890",
                "callBackUrl": "http://localhost:8080/callback"
            }
        
        """.trimIndent()

        RestAssured.given()
            .contentType("application/json")
            .body(requestBody)
            .`when`().post("/charging-session/start")
            .then()
            .statusCode(202)
            .body("status", CoreMatchers.`is`("accepted"))
            .body(
                "message",
                CoreMatchers.`is`("Request is being processed asynchronously. The result will be sent to the provided callback URL.")
            )
            .body("timestamp", Matchers.notNullValue())
    }
}