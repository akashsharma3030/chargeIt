package com.chargeIt.driver.register.api

import com.chargeIt.driver.register.api.model.ChargeSessionModel
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.context.ManagedExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.Logger
import java.time.Instant

class ChargingSessionControllerTest {

    private lateinit var controller: ChargingSessionController
    private lateinit var mockService: ChargeSessionService
    private lateinit var mockExecutor: ManagedExecutor
    private lateinit var mockLogger: Logger

    @BeforeEach
    fun setup() {
        mockService = mock(ChargeSessionService::class.java)
        mockExecutor = mock(ManagedExecutor::class.java) // Mocking ManagedExecutor
        mockLogger = mock(Logger::class.java)

        // Stub runAsync to execute the Runnable immediately
        `when`(mockExecutor.runAsync(any())).thenAnswer { invocation ->
            val runnable = invocation.getArgument<Runnable>(0)
            runnable.run()
            null
        }

        controller = ChargingSessionController(mockService, mockExecutor, mockLogger)
        }


    @Test
    fun `start should return accepted response and log info`() {
        // Given
        val session = ChargeSessionModel(
            11111111111111L,
            "charger-123",
            "http://example.com/callback"
        )

        // When
        val response = controller.start(session)

        // Then
        assertEquals(Response.Status.ACCEPTED.statusCode, response.status)
        val responseBody = response.entity as Map<*, *>
        assertEquals("accepted", responseBody["status"])
        assertEquals(
            "Request is being processed asynchronously. The result will be sent to the provided callback URL.",
            responseBody["message"]
        )
        assert(responseBody["timestamp"] is Instant)

        verify(mockLogger).info(contains("Received request to start charging session"), eq(session))
        verify(mockExecutor).runAsync(any())
    }

    @Test
    fun `processSessionAsync should log error when exception occurs`() {
        // Given
        val session = ChargeSessionModel(
            123456789L,
            "charger-123",
            "http://example.com/callback"
        )
        doThrow(RuntimeException("Service error")).`when`(mockService).sendToAuthorizationService(session)

        // When
        controller.start(session)

        // Then
        verify(mockLogger).error(contains("Error processing session"), eq(session), any())
    }
}