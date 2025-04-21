package com.chargeIt.driver.authorize.infra


import com.chargeIt.driver.authorize.service.dto.AuthorizeDriverReqDto
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.Logger

class CallBackRestClientServiceImplTest {

    private val callBackRestClient = mock(CallBackRestClient::class.java)
    private val logger = mock(Logger::class.java)
    private val service = CallBackRestClientServiceImpl(callBackRestClient,logger)

    @Test
    fun `should call submitCallBackData with correct parameters`() {
        val reqDto = AuthorizeDriverReqDto(1L, "token123", "http://callback.url",true)
        `when`(callBackRestClient.submitCallBackData(anyString(), anyString())).thenReturn("Success")
        service.initiateCallBackProcess(reqDto)
        verify(callBackRestClient).submitCallBackData(anyString(), anyString())
    }

}