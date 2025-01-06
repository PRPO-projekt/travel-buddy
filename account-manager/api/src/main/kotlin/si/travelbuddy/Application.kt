package si.travelbuddy

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.swagger.io.Authentication
import si.travelbuddy.plugins.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            val secret = this@configureSecurity.environment.config.property("jwt.secret").getString()
            val issuer = this@configureSecurity.environment.config.property("jwt.issuer").getString()
            val audience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            val myRealm = this@configureSecurity.environment.config.property("jwt.realm").getString()
        }
    }
}
