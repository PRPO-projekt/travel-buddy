package si.travelbuddy.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.nio.file.Paths

fun Application.configureHTTP() {
    routing {
        swaggerUI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
        // openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    }
}
