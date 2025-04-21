package com.chargeIt.driver.register.service

import com.chargeIt.driver.register.api.ChargeSessionService
import com.chargeIt.driver.register.api.model.ChargeSessionModel
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger

@ApplicationScoped
class ChargeSessionServiceImpl(
    private val authorizationQueue: AuthorizationQueue,
    private val logger: Logger
) : ChargeSessionService {

    override fun sendToAuthorizationService(session: ChargeSessionModel) {
        logger.info("Initiating process to send session to AuthorizationQueue: {}", session)
        runBlocking {
            try {
                sendSessionToQueue(session)
                receiveAcknowledgment()
                logger.info("Session successfully processed: {}", session)
            } catch (e: Exception) {
                logger.error("Error occurred while processing session: {}", session, e)
            }
        }
    }

    private suspend fun sendSessionToQueue(session: ChargeSessionModel) {
        logger.debug("Sending session to queue: {}", session)
        authorizationQueue.send(session)
        logger.debug("Session sent to queue successfully: {}", session)
    }

    private suspend fun receiveAcknowledgment() {
        logger.debug("Waiting for acknowledgment from queue...")
        val acknowledgment = authorizationQueue.receive()
        logger.debug("Acknowledgment received: {}", acknowledgment)
    }
}