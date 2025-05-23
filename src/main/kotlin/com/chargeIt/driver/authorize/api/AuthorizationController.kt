package com.chargeIt.driver.authorize.api

import com.chargeIt.driver.authorize.api.model.AuthorizeDriverReqModel
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.slf4j.Logger

@ApplicationScoped
@Path("/authorize/driver")
class AuthorizationController @Inject constructor(
    private val logger: Logger,
    private val authorizeDriverService: AuthorizeDriverService
) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun authorizeDriver(reqModel: AuthorizeDriverReqModel): Response {
        logger.info("Received session: $reqModel")
        val isAuthorized = authorizeDriverService.processDriverAuthorization(reqModel)
        val message = if (isAuthorized) "Driver authorized successfully" else "Driver authorization failed"

        return Response.ok(message).build()
    }
}