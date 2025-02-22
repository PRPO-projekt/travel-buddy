package si.travelbuddy

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import si.travelbuddy.plugins.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun main() {
    embeddedServer(Netty, port = 8091, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}

