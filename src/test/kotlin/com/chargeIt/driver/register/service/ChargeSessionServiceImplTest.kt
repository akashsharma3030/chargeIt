package com.chargeIt.driver.register.service

import com.chargeIt.driver.register.api.model.ChargeSessionModel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.Logger

class ChargeSessionServiceImplTest {

    private lateinit var service: ChargeSessionServiceImpl
    private lateinit var mockQueue: AuthorizationQueue
    private lateinit var mockLogger: Logger

    @BeforeEach
    fun setup() {
        mockQueue = mock(AuthorizationQueue::class.java)
        mockLogger = mock(Logger::class.java)
        service = ChargeSessionServiceImpl(mockQueue, mockLogger)
    }

    @Test
    fun `sendToAuthorizationService should process session successfully`() = runBlocking {
        // Given
        val session = ChargeSessionModel(123456789L, "charger-123", "http://example.com/callback")

        // When
        service.sendToAuthorizationService(session)

        // Then
        verify(mockLogger).info(contains("Initiating process to send session to AuthorizationQueue"), eq(session))
        verify(mockQueue).send(session)
        verify(mockLogger).info(contains("Session successfully processed"), eq(session))
    }

    @Test
    fun `sendToAuthorizationService should log error when exception occurs`() = runBlocking {
        // Given
        val session = ChargeSessionModel(123456789L, "charger-123", "http://example.com/callback")
        doThrow(RuntimeException("Queue error")).`when`(mockQueue).send(session)

        // When
        service.sendToAuthorizationService(session)

        // Then
        verify(mockLogger).info(contains("Initiating process to send session to AuthorizationQueue"), eq(session))
        verify(mockQueue).send(session)
        verify(mockLogger).error(contains("Error occurred while processing session"), eq(session), any())
    }
}