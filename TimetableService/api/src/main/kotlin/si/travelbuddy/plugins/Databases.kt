package si.travelbuddy.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import si.travelbuddy.*

fun Application.configureDatabases() {
    val dbPass: String? = System.getenv("DB_PASS")
    if (dbPass.isNullOrBlank()) {
        throw IllegalArgumentException("DB_PASS env variable missing")
    }

    val database = Database.connect(
        url = "jdbc:postgresql://traverl-buddy.postgres.database.azure.com:5432/",
        user = "prpo",
        password = dbPass
    )

    val stopService = StopService(database)
    val routeService = RouteService(database)
    val tripService = TripService(database)
    val stopTimeService = StopTimeService(database)

    routing {
        stops(stopService, stopTimeService)
        trips(tripService)
        gtfsImportRoute(stopService, routeService, tripService, stopTimeService)
    }
}
