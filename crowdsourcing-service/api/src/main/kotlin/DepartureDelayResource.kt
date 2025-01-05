package si.travelbuddy.crowdsourcing

import DepartureDelayDao
import DepartureDelayService
import dto.DepartureDelayDto
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.transactions.transaction

@Resource("/delay")
class DepartureDelayResource(val stopId: String? = null) {
    @Resource("{id}")
    class Id(val parent: DepartureDelayResource = DepartureDelayResource(), val id: Int)

    @Resource("average")
    class Average(val parent: DepartureDelayResource = DepartureDelayResource())
}

fun Route.departureDelay(departureService: DepartureDelayService) {
    get<DepartureDelayResource> { dep ->
        call.respond(transaction {
            DepartureDelayDao.all().toList()
        }.map { it.toModel() }.filter { dep.stopId == null || it.stopId == dep.stopId })
    }

    get<DepartureDelayResource.Average> { average ->
        call.respond(departureService.averageDelay())
    }

    get<DepartureDelayResource.Id> { id ->
        call.respond(transaction {
            val delay = DepartureDelayDao.findById(id.id)
            delay?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    post("/delay") {
        val dto = call.receive<DepartureDelayDto>()
        val created = departureService.create(dto)
        call.respond(HttpStatusCode.Created, created.id.value)
    }
}