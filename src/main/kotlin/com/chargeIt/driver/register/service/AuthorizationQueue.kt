package com.chargeIt.driver.register.service

import com.chargeIt.driver.register.api.model.ChargeSessionModel

interface AuthorizationQueue {
    suspend fun send(session: ChargeSessionModel)
    suspend fun receive(): ChargeSessionModel
}