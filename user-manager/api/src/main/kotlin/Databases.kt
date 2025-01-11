package si.travelbuddy.userManager

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import si.travelbuddy.UserSavedPoiService
import si.travelbuddy.UserSavedTripService
import si.travelbuddy.UserService
import si.travelbuddy.UserSettingsService

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

    val userService = UserService(database)
    val userSettingsService = UserSettingsService(database)
    val userSavedTripService = UserSavedTripService(database)
    val userSavedPoiService = UserSavedPoiService(database)

    routing {
        users(userService)
        userSettings(userSettingsService)
        userTrips(userSavedTripService)
        userPois(userSavedPoiService)
    }
}
