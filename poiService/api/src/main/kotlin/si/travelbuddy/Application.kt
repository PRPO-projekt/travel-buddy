package si.travelbuddy

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import si.travelbuddy.plugins.configureDatabases
import si.travelbuddy.plugins.configureHTTP
import si.travelbuddy.plugins.configureMonitoring
import si.travelbuddy.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Resources)

    install(CORS) {
        anyHost()
    }

    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}
