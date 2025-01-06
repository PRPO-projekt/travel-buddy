package si.travelbuddy

import io.ktor.resources.*
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import si.travelbuddy.dto.RegisterUserDto

@Resource("/register")
class RegisterResource () {}
@Resource("/login")
class LoginResource () {}

fun Route.auth(service: AuthService) {
    post<RegisterResource> {
        val user = call.receive<RegisterUserDto>()
        call.respondText(service.create(user).toString())
    }

    post<LoginResource> {
//        val user = call.receive<>()
    }
}