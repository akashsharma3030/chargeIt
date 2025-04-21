package com.chargeIt.driver.authorize.service.dto

data class AuthorizeDriverReqDto(
    val stationId: Long = 0L,
    val driverToken: String = "",
    val callBackUrl: String = "",
    val status: Boolean = false,
)
