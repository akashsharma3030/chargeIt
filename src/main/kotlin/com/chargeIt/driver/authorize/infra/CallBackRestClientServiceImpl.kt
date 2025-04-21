package com.chargeIt.driver.authorize.infra

import com.chargeIt.driver.authorize.service.CallBackRestClientService
import com.chargeIt.driver.authorize.service.dto.AuthorizeDriverReqDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.UriBuilder
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class CallBackRestClientServiceImpl @Inject constructor(
    @RestClient private val callBackRestClient: CallBackRestClient,
    private val logger: org.slf4j.Logger
) : CallBackRestClientService {


    override fun initiateCallBackProcess(authorizeDriverReqDto: AuthorizeDriverReqDto) {
        val callBackUrl = authorizeDriverReqDto.callBackUrl
        try {
            val callBackData = CallBackData(
                station_id = authorizeDriverReqDto.stationId,
                driver_token = authorizeDriverReqDto.driverToken,
                status = authorizeDriverReqDto.status
            )
            val jsonData = ObjectMapper().writeValueAsString(callBackData) // Serialize to JSON
            val response = callBackRestClient.submitCallBackData(
                UriBuilder.fromUri(callBackUrl).build().toString(), // Use dynamic URL
                jsonData
            )
            logger.info("Callback response: {}", response)
        } catch (e: Exception) {
            logger.error("Error during callback process: {}", e.message, e)
        }
    }

    data class CallBackData(
        val station_id: Long,
        val driver_token: String,
        val status: Boolean
    )
}