package com.chargeIt.driver.register.api

import com.chargeIt.driver.register.api.model.ChargeSessionModel

interface ChargeSessionService {
        fun sendToAuthorizationService(chargeSessionModel: ChargeSessionModel)
}