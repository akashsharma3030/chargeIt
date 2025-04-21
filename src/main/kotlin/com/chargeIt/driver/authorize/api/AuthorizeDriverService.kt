package com.chargeIt.driver.authorize.api

import com.chargeIt.driver.authorize.api.model.AuthorizeDriverReqModel

interface AuthorizeDriverService {
    fun processDriverAuthorization(reqModel: AuthorizeDriverReqModel): Boolean
}