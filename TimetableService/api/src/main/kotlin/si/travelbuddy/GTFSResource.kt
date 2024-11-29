package si.travelbuddy

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*

import org.onebusaway.csv_entities.EntityHandler
import org.onebusaway.csv_entities.FileCsvInputSource
import org.onebusaway.gtfs.model.Stop
import org.onebusaway.gtfs.serialization.*
import si.travelbuddy.dto.StopDto
import kotlin.io.path.writeBytes
import kotlin.io.path.writer

private class GtfsEntityHandler(private val service: StopService) : EntityHandler {
    public override fun handleEntity(p0: Any) {
        if (p0 is Stop) {
            val stop = p0 as Stop;
            val stopDto = StopDto(
                stopId = stop.id.toString(),
                code = stop.code,
                name = stop.name,
                desc = stop.desc,
                lat = stop.lat,
                lon = stop.lon
            )

            service.create(stopDto)
        }
    }
}

fun Route.gtfsImportRoute(service: StopService) {
    route("/gtfs") {
        post("/import") {
            val gtfsReader = GtfsReader()

            val file = kotlin.io.path.createTempFile(prefix = "temp_import", suffix = ".zip");
            file.writeBytes(call.receive<ByteArray>());

            val entityHandler = GtfsEntityHandler(service);
            gtfsReader.addEntityHandler(entityHandler);

            gtfsReader.run(FileCsvInputSource(file.toFile()));
        }
    }
}
