package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.dto.StopTimeDto
import si.travelbuddy.entity.*
import java.time.LocalTime
import java.util.*

@Resource("/stops")
class StopsResource(
    /* This parameter accepts a regular expression filter */
    val name: String = ".*"
) {
    @Resource("{id}")
    class Id(val parent: StopsResource = StopsResource(), val id: String) {
        @Resource("departures")
        class Departures(val parent: Id,
                         val from: Long = LocalTime.MIN.toSecondOfDay().toLong(),
                         val until: Long = LocalTime.MAX.toSecondOfDay().toLong()) {
            @Resource("{depId}")
            class DepId(val parent: Departures, val id: String) {
            }
        }
    }
}

fun Route.stops(stopService: StopService, stopTimeService: StopTimeService) {
    /*
    Retrieve all stops. Use the `name` parameter to filter stops by name.
     */
    get<StopsResource> { stop ->
        call.respond(transaction {
            StopDao.all().toList()
        }.map { dao -> dao.toModel() }.filter { s -> Regex(stop.name).matches(s.name) })
    }

    /*
    Retrieve stop with specified ID
     */
    get<StopsResource.Id> { stopId ->
        call.respond(transaction {
            val stop = StopDao.findById(stopId.id)

            stop?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    /*
    Update stop with specified ID
     */
    put<StopsResource.Id> { stop ->
        if (StopDao.findById(stop.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        val stopDto = call.receive<StopDto>();
        stopService.update(stop.id, stopDto)
        call.respond(HttpStatusCode.OK)
    }

    /*
    Delete stop with specified ID
     */
    delete<StopsResource.Id> { stop ->
        if (StopDao.findById(stop.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        stopService.delete(stop.id)
        call.respond(HttpStatusCode.OK)
    }

    /*
    Add new stop
     */
    post<StopsResource.Id> { stop ->
        val stopDto = call.receive<StopDto>()
        call.respondText(stopService.create(stopDto))
    }

    @Serializable
    data class Departure(
        val uuid: String,
        val depTime: String,
        val trip: Trip,
        val route: si.travelbuddy.entity.Route,
    )

    @Serializable
    data class Departures(
        val stop: Stop? = null,
        val departures: List<Departure>
    )

    /*
    Retrieve departures for stop with specified ID. Use the `from` and `until` parameters to filter by departure time.
     */
    get<StopsResource.Id.Departures> { dep ->
        call.respond(transaction {
            val res = mutableListOf<Departure>()

            val stopTimes = StopTimeDao.find { StopTimeTable.stopId eq dep.parent.id }.toList()

            for (stopTime in stopTimes.filter {
                it.departureTime > LocalTime.ofSecondOfDay(dep.from) && it.departureTime < LocalTime.ofSecondOfDay(dep.until)
            }) {
                val trip = stopTime.tripId
                val route = trip.routeId

                res.add(Departure(stopTime.id.value.toString(), stopTime.departureTime.toString(), trip.toModel(), route.toModel()))
            }

            res.sortBy { it.depTime }

            Departures(StopDao.findById(dep.parent.id)?.toModel(), res.toList())
        })
    }


    post<StopsResource.Id.Departures> { dep ->
        val stopTimeDto = call.receive<StopTimeDto>()
        call.respondText(stopTimeService.create(stopTimeDto).toString())
    }

    get<StopsResource.Id.Departures.DepId> { id ->
        call.respond(transaction {
            var uuid: UUID? = null
            try {
                uuid = UUID.fromString(id.id)
            }
            catch (e: IllegalArgumentException) {
                HttpStatusCode.BadRequest
            }

            val stopTime = StopTimeDao.findById(uuid!!)
            if (stopTime == null) {
                HttpStatusCode.NotFound
            }

            if (stopTime!!.stopId.id.value != id.parent.parent.id) {
                HttpStatusCode.BadRequest
            }

            stopTime
        })
    }

}