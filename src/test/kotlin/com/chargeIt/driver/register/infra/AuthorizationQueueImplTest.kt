package com.chargeIt.driver.register.infra

import com.chargeIt.driver.common.AuthorizationRestClient
import com.chargeIt.driver.register.api.model.ChargeSessionModel
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.Logger


class AuthorizationQueueImplTest {

    private lateinit var queue: AuthorizationQueueImpl
    private lateinit var mockRestClient: AuthorizationRestClient
    private lateinit var mockObjectMapper: ObjectMapper
    private lateinit var mockLogger: Logger
    private lateinit var mockChannel: Channel<ChargeSessionModel>

    @BeforeEach
    fun setup() {
        mockRestClient = mock()
        mockObjectMapper = mock()
        mockLogger = mock()
        mockChannel = mock() // Type-safe mocking

        queue = AuthorizationQueueImpl(
            mockRestClient,
            mockObjectMapper,
            5000L,
            mockLogger,
            mockChannel
        )
    }

    @Test
    fun `send should send session to channel and log info`() {
        runBlocking {
            // Given
            val session = ChargeSessionModel(123456789L, "charger-123", "http://example.com/callback")

            // When
            queue.send(session)

            // Then
            verify(mockChannel).send(session)
        }
    }

    @Test
    fun `receive should process session and log info`() {
        runBlocking {
            // Given
            val session = ChargeSessionModel(123456789L, "charger-123", "http://example.com/callback")
            whenever(mockChannel.receive()).thenReturn(session)
            val jsonString = """{"driverToken":123456789,"chargerId":"charger-123","callBackUrl":"http://example.com/callback"}"""
            whenever(mockObjectMapper.writeValueAsString(session)).thenReturn(jsonString)

            // When
            queue.receive()

            // Then
            verify(mockChannel).receive()
        }
    }

}