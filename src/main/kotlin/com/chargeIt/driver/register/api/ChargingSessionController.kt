package com.chargeIt.driver.register.api

import com.chargeIt.driver.register.api.model.ChargeSessionModel
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.context.ManagedExecutor
import org.slf4j.Logger
import java.time.Instant

@Path("/charging-session")
@ApplicationScoped
class ChargingSessionController @Inject constructor(
    private val service: ChargeSessionService,
    private val executor: ManagedExecutor,
    private val logger: Logger
) {

    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun start(@Valid session: ChargeSessionModel): Response {
        logger.info("Received request to start charging session: {}", session)

        processSessionAsync(session)

        return buildAcceptedResponse()
    }

    private fun processSessionAsync(session: ChargeSessionModel) {
        executor.runAsync {
            try {
                service.sendToAuthorizationService(session)
                logger.info("Service method executed in background for session: {}", session)
            } catch (e: Exception) {
                logger.error("Error processing session: {}", session, e)
            }
        }
    }

    private fun buildAcceptedResponse(): Response {
        val responseBody = mapOf(
            "status" to "accepted",
            "message" to "Request is being processed asynchronously. The result will be sent to the provided callback URL.",
            "timestamp" to Instant.now()
        )

        return Response.accepted()
            .entity(responseBody)
            .build()
    }
}