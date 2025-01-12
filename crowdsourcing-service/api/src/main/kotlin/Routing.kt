package si.travelbuddy.crowdsourcing

import io.ktor.server.application.*
import io.ktor.server.resources.Resources

fun Application.configureRouting() {
    install(Resources)
}
