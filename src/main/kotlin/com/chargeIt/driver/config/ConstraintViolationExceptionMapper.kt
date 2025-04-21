package com.chargeIt.driver.config

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ConstraintViolationExceptionMapper : ExceptionMapper<ConstraintViolationException> {

    override fun toResponse(exception: ConstraintViolationException): Response {
        val errors = exception.constraintViolations.map {
            mapOf(
                "field" to it.propertyPath.toString(),
                "message" to it.message
            )
        }
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(mapOf(
                "timestamp" to java.time.Instant.now(),
                "errors" to errors
            ))
            .build()
    }
}