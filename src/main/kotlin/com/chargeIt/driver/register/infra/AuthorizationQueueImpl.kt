package com.chargeIt.driver.register.infra

import com.chargeIt.driver.common.AuthorizationRestClient
import com.chargeIt.driver.register.api.model.ChargeSessionModel
import com.chargeIt.driver.register.service.AuthorizationQueue
import com.fasterxml.jackson.databind.ObjectMapper
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.slf4j.Logger

@ApplicationScoped
class AuthorizationQueueImpl @Inject constructor(
    @RestClient private val authorizationRestClient: AuthorizationRestClient,
    private val objectMapper: ObjectMapper,
    @ConfigProperty(name = "queue.timeout.duration", defaultValue = "5000")
    private val timeoutDurationMs: Long,
    private val logger: Logger,
    private val channel: Channel<ChargeSessionModel>
) : AuthorizationQueue {

    override suspend fun send(session: ChargeSessionModel) {
        logger.info("Sending session to the queue: {}", session)
        channel.send(session)
    }

    override suspend fun receive(): ChargeSessionModel {
        val session = channel.receive()
        logger.info("Received session from the queue: {}", session)

        try {
            processSession(session)
        } catch (e: Exception) {
            logger.error("Error processing session: {}", e.message, e)
            throw e
        }

        return session
    }

    private suspend fun processSession(session: ChargeSessionModel) {
        validateSession(session)
        withTimeout(timeoutDurationMs) {
            val sessionJson = objectMapper.writeValueAsString(session)
            logger.debug("Serialized session JSON: {}", sessionJson)

            val response = authorizationRestClient.submitData(sessionJson)
                ?.onItem()?.ifNotNull()?.transform { "Processed: $it" }
                ?.onItem()?.ifNull()?.continueWith("Fallback response")
                ?.awaitSuspending()

            logger.info("Processed response: {}", response)
        }
    }

    private fun validateSession(session: ChargeSessionModel) {
        requireNotNull(session.driverToken) { "Session ID must not be null" }
        requireNotNull(session.callBackUrl) { "Callback URL must not be null" }
    }
}