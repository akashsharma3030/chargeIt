package com.chargeIt.driver.register.api.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
@JsonSerialize
@JsonDeserialize
data class ChargeSessionModel(
    @field:NotNull(message = "stationIdentifier cannot be null") val stationId: Long =0L,
    @field:NotNull(message = "driverIdentifier cannot be null")
    @field:Pattern(
        regexp = "^[A-Za-z0-9\\-._~]{20,80}$",
        message = "driverIdentifier must be 20 to 80 characters long and can only contain uppercase letters, lowercase letters, digits, hyphen (-), period (.), underscore (_), and tilde (~)"
    ) val driverToken: String="",
    @field:NotBlank(message = "callBackUrl cannot be null or blank")
    @field:org.hibernate.validator.constraints.URL(message = "callBackUrl must be a valid URL")
    val callBackUrl: String = ""
)