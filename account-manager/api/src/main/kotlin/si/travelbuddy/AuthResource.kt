package si.travelbuddy

import io.ktor.http.HttpStatusCode
import io.ktor.resources.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.LoginUserDto
import si.travelbuddy.dto.RegisterUserDto

fun Route.auth(authService: AuthService) {
    post("/register") {
        val userDto = call.receive<RegisterUserDto>()
        val user = authService.register(userDto)

        if (user != null) call.respond(HttpStatusCode.OK, message = user)
        else call.respond(HttpStatusCode.Forbidden, message = "Invalid user data")
    }

    post("/login") {
        val userDto = call.receive<LoginUserDto>()
        val user = authService.login(userDto)
        if (user != null) call.respond(HttpStatusCode.OK, message = user)
        else call.respond(HttpStatusCode.Forbidden, message = "Incorrect credentials")
    }
}