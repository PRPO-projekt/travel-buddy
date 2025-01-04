package si.travelbuddy.crowdsourcing

import DepartureDelayService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val dbPass = environment.config.propertyOrNull(path = "database.pass")?.getString()
        ?: throw RuntimeException("Database password missing!")

    val database = Database.connect(
        url = "jdbc:postgresql://traverl-buddy.postgres.database.azure.com:5432/",
        user = "prpo",
        password = dbPass
    )

    val depService = DepartureDelayService(database)

    routing {
        departureDelay(depService)
    }
}
