package com.chargeIt.driver.authorize.service

import com.chargeIt.driver.authorize.api.AuthorizeDriverService
import com.chargeIt.driver.authorize.api.model.AuthorizeDriverReqModel
import com.chargeIt.driver.authorize.service.dto.AuthorizeDriverReqDto
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class AuthorizeDriverServiceImpl @Inject constructor(
    private val callBackRestClientService: CallBackRestClientService,
    @ConfigProperty(name = "allowed.drivers") private val allowedDrivers: List<String>,
    @ConfigProperty(name = "allowed.station.id") private val allowedStationId: Long,
    private val logger: Logger = LoggerFactory.getLogger(AuthorizeDriverServiceImpl::class.java)
) : AuthorizeDriverService {

    private val authorizationResults = mutableMapOf<DriverStationKey, Boolean>() // Map to store results

    override fun processDriverAuthorization(reqModel: AuthorizeDriverReqModel): Boolean {
        val isAuthorized = isDriverAuthorizeToCharge(reqModel)
        val key = DriverStationKey(reqModel.driverToken, reqModel.stationId)
        authorizationResults[key] = isAuthorized // Save result in the map

        // Call the CallBackRestClientService method
        val callBackDto = AuthorizeDriverReqDto(reqModel.stationId, reqModel.driverToken, reqModel.callBackUrl, isAuthorized)
        callBackRestClientService.initiateCallBackProcess(callBackDto)

        return isAuthorized
    }

    private fun isDriverAuthorizeToCharge(reqModel: AuthorizeDriverReqModel): Boolean {
        val isDriverAllowed = allowedDrivers.contains(reqModel.driverToken)
        val isStationAllowed = reqModel.stationId == allowedStationId

        val isAuthorized = isDriverAllowed && isStationAllowed

        logger.info(
            "Authorization result: driverIdentifier={}, stationIdentifier={}, isAuthorized={}",
            reqModel.driverToken, reqModel.stationId, isAuthorized
        )

        return isAuthorized
    }

    data class DriverStationKey(val driverId: String, val stationId: Long)
}