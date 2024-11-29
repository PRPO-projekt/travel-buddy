package si.travelbuddy

import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.swagger.models.properties.PropertyBuilder.toModel
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.entity.StopDAO
import si.travelbuddy.entity.StopTable

@Resource("/stops")
class StopsResource {
    @Resource("{id}")
    class Id(val parent: StopsResource = StopsResource(), val id: String)
}

fun Route.stopsRoute() {
    get<StopsResource> { stop ->
        call.respond(transaction {
            StopDAO.all().toList()
        }.map { dao -> dao.toModel() })
    }
    get<StopsResource.Id> { stop ->
        call.respond(transaction {
            StopDAO.find { StopTable.stopId eq stop.id }.toList()
        }.map { dao -> dao.toModel() })
    }
}