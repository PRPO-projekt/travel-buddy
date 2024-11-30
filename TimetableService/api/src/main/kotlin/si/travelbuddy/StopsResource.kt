package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.entity.*
import java.time.LocalTime

@Resource("/stops")
class StopsResource(val name: String = ".*") {
    @Resource("{id}")
    class Id(val parent: StopsResource = StopsResource(), val id: String) {
        @Resource("departures")
        class Departures(val parent: Id)
    }

}

fun Route.stopsRoute(service: StopService) {
    get<StopsResource> { stop ->
        call.respond(transaction {
            StopDAO.all().toList()
        }.map { dao -> dao.toModel() }.filter { s -> Regex(stop.name).matches(s.name) })
    }
    get<StopsResource.Id> { stop ->
        call.respond(transaction {
            StopDAO.find { StopTable.id eq stop.id }.toList()
        }.map { dao -> dao.toModel() })
    }
    put<StopsResource.Id> { stop ->
        val stopDto = call.receive<StopDto>();
        service.update(stopDto)
    }
    delete<StopsResource.Id> { stop ->
        service.delete(stop.id)
    }

    @Serializable
    data class Departure(
        val depTime: String,
        val routeName: String? = null,
    )

    get<StopsResource.Id.Departures> { dep ->
        call.respond(transaction {
            val stop = StopDAO.findById(dep.parent.id)
            if (stop == null) {
                "Could not find stop with id ${dep.parent.id}"
            }
            else {
                val res = mutableListOf<Departure>()

                val stopTimes = StopTimeDAO.find { StopTimeTable.stopId eq stop.id }.toList()

                for (stopTime in stopTimes) {
                    val trip = TripDAO.findById(stopTime.id.value[StopTimeTable.tripId].toString())
                    val route = trip?.routeId

                    if (route != null) {
                        res.add(Departure(stopTime.departureTime.toString(), route.routeLongName))
                    }
                }

                res
            }
        })
    }
}