package com.chargeIt.driver.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import kotlinx.coroutines.channels.Channel
import com.chargeIt.driver.register.api.model.ChargeSessionModel

@ApplicationScoped
class ChannelProducer {

    @Produces
    fun produceChargeSessionChannel(): Channel<ChargeSessionModel> {
        return Channel(Channel.UNLIMITED) // Adjust capacity as needed
    }
}