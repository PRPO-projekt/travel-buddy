package si.travelbuddy

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.put

import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.TripDto
import si.travelbuddy.entity.StopTimeDao
import si.travelbuddy.entity.StopTimeTable
import si.travelbuddy.entity.TripDao

@Resource("/trips")
class TripsResource {
    @Resource("{id}")
    class Id(val parent: TripsResource = TripsResource(), val id: String) {
        @Resource("stops")
        class Stops(val parent: Id)
    }
}

fun Route.trips(service: TripService) {
    /*
    Retrieve all trips
     */
    get<TripsResource> { trip ->
        call.respond(transaction {
            TripDao.all().toList()
        }.map { dao -> dao.toModel() })
    }

    /*
    Retrieve trip with specified ID
     */
    get<TripsResource.Id> { tripId ->
        call.respond(transaction {
            val trip = TripDao.findById(tripId.id)

            trip?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    /*
    Delete trip with specified ID
     */
    delete<TripsResource.Id> { tripId ->
        if (TripDao.findById(tripId.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        service.delete(tripId.id)
        call.respond(HttpStatusCode.OK)
    }

    /*
    Update trip with specified ID
     */
    put<TripsResource.Id> { tripId ->
        if (TripDao.findById(tripId.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        val trip = call.receive<TripDto>()
        service.update(tripId.id, trip)
        call.respond(HttpStatusCode.OK)
    }

    /*
    Retrieve stops for trip with specified ID
     */
    get<TripsResource.Id.Stops> { stop ->
        call.respond(transaction {
            val trip = TripDao.findById(stop.parent.id)

            if (trip == null) {
                HttpStatusCode.NotFound
            }
            else {
                val stops = StopTimeDao.find { StopTimeTable.tripId eq stop.parent.id }
                    .map { it.stopId }
                    .map { it.toModel() }
                    .toList()

                stops
            }
        })
    }
}