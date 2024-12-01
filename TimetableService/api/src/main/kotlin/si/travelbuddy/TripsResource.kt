package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.entity.StopTimeDAO
import si.travelbuddy.entity.StopTimeTable
import si.travelbuddy.entity.TripDAO

@Resource("/trips")
class TripsResource {
    @Resource("{id}")
    class Id(val parent: TripsResource = TripsResource(), val id: String) {
        @Resource("stops")
        class Stops(val parent: Id)
    }
}

fun Route.tripRoute() {
    get<TripsResource> { trip ->
        call.respond(transaction {
            TripDAO.all().toList()
        }.map { dao -> dao.toModel() })
    }
    get<TripsResource.Id> { tripId ->
        val trip = transaction { TripDAO.findById(tripId.id) }

        if (trip == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        else {
            call.respond(HttpStatusCode.OK, trip.toModel())
        }
    }
    get<TripsResource.Id.Stops> { stop ->
        call.respond(transaction {
            val trip = TripDAO.findById(stop.parent.id)

            if (trip == null) {
                HttpStatusCode.NotFound
            }
            else {
                val stops = StopTimeDAO.find { StopTimeTable.tripId eq stop.parent.id }
                    .map { it.stopId }
                    .map { it.toModel() }
                    .toList()

                stops
            }
        })
    }
}