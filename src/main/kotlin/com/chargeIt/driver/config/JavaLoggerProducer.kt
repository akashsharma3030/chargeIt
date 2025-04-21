package com.chargeIt.driver.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.enterprise.inject.spi.InjectionPoint
import java.util.logging.Logger

@ApplicationScoped
class JavaLoggerProducer {

    @Produces
    fun produceJavaLogger(injectionPoint: InjectionPoint): Logger {
        return Logger.getLogger(injectionPoint.member.declaringClass.name)
    }
}