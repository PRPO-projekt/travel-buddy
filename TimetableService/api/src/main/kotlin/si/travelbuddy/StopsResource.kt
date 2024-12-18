package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.entity.*
import java.time.LocalTime

@Resource("/stops")
class StopsResource(val name: String = ".*") {
    @Resource("{id}")
    class Id(val parent: StopsResource = StopsResource(), val id: String) {
        @Resource("departures")
        class Departures(val parent: Id,
                         val from: Long = LocalTime.MIN.toSecondOfDay().toLong(),
                         val until: Long = LocalTime.MAX.toSecondOfDay().toLong())
    }
}

fun Route.stops(service: StopService) {
    get<StopsResource> { stop ->
        call.respond(transaction {
            StopDao.all().toList()
        }.map { dao -> dao.toModel() }.filter { s -> Regex(stop.name).matches(s.name) })
    }

    get<StopsResource.Id> { stopId ->
        val stop = transaction { StopDao.findById(stopId.id) }

        if (stop == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        else {
            call.respond(HttpStatusCode.OK, stop.toModel())
        }
    }
    put<StopsResource.Id> { stop ->
        val stopDto = call.receive<StopDto>();
        service.update(stop.id, stopDto)
    }

    delete<StopsResource.Id> { stop ->
        service.delete(stop.id)
    }

    @Serializable
    data class Departure(
        val depTime: String,
        val trip: Trip,
        val route: si.travelbuddy.entity.Route,
    )

    @Serializable
    data class Departures(
        val stop: Stop? = null,
        val departures: List<Departure>
    )

    get<StopsResource.Id.Departures> { dep ->
        call.respond(transaction {
            val res = mutableListOf<Departure>()

            val stopTimes = StopTimeDao.find { StopTimeTable.stopId eq dep.parent.id }.toList()

            for (stopTime in stopTimes.filter {
                it.departureTime > LocalTime.ofSecondOfDay(dep.from) && it.departureTime < LocalTime.ofSecondOfDay(dep.until)
            }) {
                val trip = stopTime.tripId
                val route = trip.routeId

                res.add(Departure(stopTime.departureTime.toString(), trip.toModel(), route.toModel()))
            }

            res.sortBy { it.depTime }

            Departures(StopDao.findById(dep.parent.id)?.toModel(), res.toList())
        })
    }
}