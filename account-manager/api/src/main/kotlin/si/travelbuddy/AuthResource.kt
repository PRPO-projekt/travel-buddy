package si.travelbuddy

import io.ktor.resources.*
import io.ktor.server.routing.Route

@Resource("/register")
class RegisterResource () {}
@Resource("/login")
class LoginResource () {}

fun Route.auth(service: AuthService) {
}