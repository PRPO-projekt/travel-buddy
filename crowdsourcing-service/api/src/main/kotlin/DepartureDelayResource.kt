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
import org.jetbrains.exposed.sql.transactions.transaction

@Resource("/departureDelay")
class DepartureDelayResource() {
    @Resource("{id}")
    class Id(val id: Int)

    @Resource("average")
    class Average()
}

fun Route.departureDelay(departureService: DepartureDelayService) {
    get<DepartureDelayResource> {
        call.respond(transaction {
            DepartureDelayDao.all().toList()
        }.map { it.toModel() })
    }

    post("/departureDelay") {
        val dto = call.receive<DepartureDelayDto>()
        val created = departureService.create(
            DepartureDelayDto(dto.expectedTime, dto.actualTime, dto.userId)
        )
        call.respond(HttpStatusCode.Created, created.userId!!)
    }

    get<DepartureDelayResource.Id> { id ->
        call.respond(transaction {
            val delay = DepartureDelayDao.findById(id.id)
            delay?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    get<DepartureDelayResource.Average> { average ->
        call.respond(departureService.averageDelay())
    }
}