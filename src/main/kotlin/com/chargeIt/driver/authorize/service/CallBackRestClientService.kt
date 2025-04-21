package com.chargeIt.driver.authorize.service

import com.chargeIt.driver.authorize.service.dto.AuthorizeDriverReqDto


interface CallBackRestClientService {
    fun initiateCallBackProcess(authorizeDriverReqDto: AuthorizeDriverReqDto)
}