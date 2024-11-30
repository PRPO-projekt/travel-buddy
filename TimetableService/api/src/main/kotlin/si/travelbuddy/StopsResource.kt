package si.travelbuddy

import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.entity.*

@Resource("/stops")
class StopsResource(val name: String = ".*");

fun Route.stopsRoute() {
    get<StopsResource> { stop ->
        call.respond(transaction {
            StopDAO.all().toList()
        }.map { dao -> dao.toModel() }.filter { s -> Regex(stop.name).matches(s.name) })
    }
}