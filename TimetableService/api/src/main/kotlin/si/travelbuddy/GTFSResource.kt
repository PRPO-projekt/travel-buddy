package si.travelbuddy

import com.conveyal.gtfs.GTFSFeed
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import org.jetbrains.exposed.exceptions.ExposedSQLException
import si.travelbuddy.dto.RouteDto

import si.travelbuddy.dto.StopDto
import si.travelbuddy.dto.StopTimeDto
import si.travelbuddy.dto.TripDto
import si.travelbuddy.entity.RouteDAO
import kotlin.io.path.absolutePathString
import kotlin.io.path.writeBytes

fun Route.gtfsImportRoute(
    stopService: StopService,
    routeService: RouteService,
    tripService: TripService,
    stopTimeService: StopTimeService
) {
    route("/gtfs") {
        /*
        Upload GTFS feed as ZIP file

        Example curl command: `curl -X POST http://localhost:8080/gtfs/import --data-binary @/home/nikola/Downloads/b2b.gtfs.zip`
         */
        post("/import") {
            val file = kotlin.io.path.createTempFile(prefix = "temp_import", suffix = ".zip")
            call.application.environment.log.info("Writing zip file to ${file.absolutePathString()}")

            val channel = call.receiveChannel()
            val bytes = channel.readRemaining().readByteArray()
            file.writeBytes(bytes)

            val feed: GTFSFeed = GTFSFeed.fromFile(file.absolutePathString())
            for (stop in feed.stops.values) {
                stopService.create(StopDto(
                    id = stop.stop_id,
                    name = stop.stop_name,
                    lat = stop.stop_lat,
                    lon = stop.stop_lon
                ))
            }

            for (route in feed.routes.values) {
                routeService.create(RouteDto(route.route_id, route.route_short_name, route.route_long_name))
            }

            for (trip in feed.trips.values) {
                tripService.create(TripDto(
                    trip.trip_id, trip.route_id
                ))
            }

            for (stopTime in feed.stop_times.values) {
                try {
                    stopTimeService.create(StopTimeDto(
                        stopTime.trip_id,
                        stopTime.arrival_time,
                        stopTime.departure_time,
                        stopTime.stop_id
                    ))
                }
                catch (e: NullPointerException) {
                    continue
                }
                catch (e: ExposedSQLException) {
                    continue
                }
            }
        }
    }
}
