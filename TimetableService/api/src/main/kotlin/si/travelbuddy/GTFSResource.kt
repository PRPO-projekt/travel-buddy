package si.travelbuddy

import com.conveyal.gtfs.GTFSFeed
import com.conveyal.gtfs.model.Stop
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray

import si.travelbuddy.dto.StopDto
import java.util.zip.ZipFile
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream
import kotlin.io.path.writeBytes
import kotlin.io.path.writer

fun Route.gtfsImportRoute(service: StopService) {
    route("/gtfs") {
        /* Upload GTFS feed as zip file */
        post("/import") {
            val file = kotlin.io.path.createTempFile(prefix = "temp_import", suffix = ".zip")
            call.application.environment.log.info("Writing zip file to ${file.absolutePathString()}")

            val channel = call.receiveChannel()
            val bytes = channel.readRemaining().readByteArray()
            file.writeBytes(bytes)

            val feed: GTFSFeed = GTFSFeed.fromFile(file.absolutePathString())
            for (stop in feed.stops.values) {
                service.create(StopDto(
                    stopId = stop.stop_id,
                    name = stop.stop_name,
                    lat = stop.stop_lat,
                    lon = stop.stop_lon
                ))
            }
        }
    }
}
