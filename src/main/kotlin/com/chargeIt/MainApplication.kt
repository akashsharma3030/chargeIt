package com.chargeIt

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class MainApplication

fun main(args: Array<String>) {
    Quarkus.run(*args)
}