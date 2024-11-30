package si.travelbuddy

import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.entity.*
import si.travelbuddy.entity.StopDAO
import si.travelbuddy.entity.StopTable
import si.travelbuddy.entity.StopTimeDAO
import si.travelbuddy.entity.StopTimeTable
import java.time.LocalTime

@Resource("/stop")
class StopResource {
    @Resource("{id}")
    class Id(val parent: StopResource = StopResource(), val id: String) {
        @Resource("departures")
        class Departures(val parent: Id,
                         val from: Long = LocalTime.MIN.toSecondOfDay().toLong(),
                         val until: Long = LocalTime.MAX.toSecondOfDay().toLong())
    }
}

fun Route.stopRoute(service: StopService) {
    get<StopResource.Id> { stop ->
        call.respond(transaction {
            StopDAO.find { StopTable.id eq stop.id }.toList()
        }.map { dao -> dao.toModel() })
    }
    put<StopResource.Id> { stop ->
        val stopDto = call.receive<StopDto>();
        service.update(stopDto)
    }
    delete<StopResource.Id> { stop ->
        service.delete(stop.id)
    }

    @Serializable
    data class Departure(
        val depTime: String,
        val route: si.travelbuddy.entity.Route,
    )

    @Serializable
    data class Departures(
        val stop: Stop? = null,
        val departures: List<Departure>
    )

    get<StopResource.Id.Departures> { dep ->
        call.respond(transaction {
            val res = mutableListOf<Departure>()

            val stopTimes = StopTimeDAO.find { StopTimeTable.stopId eq dep.parent.id }.toList()

            for (stopTime in stopTimes.filter {
                it.departureTime > LocalTime.ofSecondOfDay(dep.from) && it.departureTime < LocalTime.ofSecondOfDay(dep.until)
            }) {
                val trip = stopTime.tripId
                val route = trip.routeId

                res.add(Departure(stopTime.departureTime.toString(), route.toModel()))
            }

            res.sortBy { it.depTime }

            Departures(StopDAO.findById(dep.parent.id)?.toModel(), res.toList())
        })
    }
}