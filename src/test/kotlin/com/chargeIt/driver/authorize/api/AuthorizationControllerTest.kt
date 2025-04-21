package com.chargeIt.driver.authorize.api

import com.chargeIt.driver.authorize.api.model.AuthorizeDriverReqModel
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.slf4j.Logger

class AuthorizationControllerTest {

    private lateinit var mockLogger: Logger
    private lateinit var mockAuthorizeDriverService: AuthorizeDriverService
    private lateinit var controller: AuthorizationController

    @BeforeEach
    fun setup() {
        mockLogger = mock()
        mockAuthorizeDriverService = mock()
        controller = AuthorizationController(mockLogger, mockAuthorizeDriverService)
    }

    @Test
    fun `authorizeDriver should log request and return success response`() {
        // Given
        val reqModel = AuthorizeDriverReqModel(123456L, "charger-123", "http://callback.url")
        whenever(mockAuthorizeDriverService.processDriverAuthorization(any())).thenReturn(true)

        // When
        val response = controller.authorizeDriver(reqModel)

        // Then
        assert(response.status == Response.Status.OK.statusCode)
        assert(response.entity == "Driver authorized successfully")
        verify(mockLogger).info("Received session: AuthorizeDriverReqModel(stationId=123456, driverToken=charger-123, callBackUrl=http://callback.url)")
    }
}