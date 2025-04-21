package com.chargeIt.driver.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.enterprise.inject.spi.InjectionPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class Slf4jLoggerProducer {

    @Produces
    fun produceLogger(injectionPoint: InjectionPoint): Logger {
        return LoggerFactory.getLogger(injectionPoint.member.declaringClass)
    }
}