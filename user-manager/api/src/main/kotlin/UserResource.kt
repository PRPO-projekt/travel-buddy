package si.travelbuddy.userManager

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.UserDao
import si.travelbuddy.UserSavedPoiDao
import si.travelbuddy.UserSavedPoiService
import si.travelbuddy.UserSavedTripDao
import si.travelbuddy.UserSavedTripService
import si.travelbuddy.UserService
import si.travelbuddy.UserSettingsDao
import si.travelbuddy.UserSettingsService
import si.travelbuddy.dto.UpdateUserDto

@Resource("/users")
class UsersResource {
    @Resource("{id}")
    class IdResource(val parent: UsersResource, val id: Long) {
        @Resource("settings")
        class SettingsResource(val parent: IdResource) {
            @Resource("{key}")
            class KeyResource(val parent: SettingsResource, val key: String) {}
        }

        @Resource("saved-trips")
        class SavedTripsResource(val parent: IdResource) {
            @Resource("{id}")
            class TripIdResource(val parent: SavedTripsResource, val id: Long) {}
        }

        @Resource("saved-poi")
        class SavedPoiResource(val parent: IdResource) {
            @Resource("{id}")
            class PoiIdResource(val parent: SavedPoiResource, val id: Long) {}
        }
    }
}

fun Route.users(userService: UserService) {
    get<UsersResource> {
        call.respond(transaction {
            UserDao.all().toList()
        }.map { userDao -> userDao.toModel() })
    }

    get<UsersResource.IdResource> {
        call.respond(transaction {
            UserDao.findById(it.id)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    delete<UsersResource> {
        UserDao.all().forEach { it.delete() }
        call.respond(HttpStatusCode.NoContent)
    }

    delete<UsersResource.IdResource> {
        if (UserDao.findById(it.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        userService.deleteUser(it.id)
        call.respond(HttpStatusCode.NoContent)
    }

    post<UsersResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    post<UsersResource.IdResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource> {
        val user = call.receive<UpdateUserDto>()
        userService.updateUser(user)
        call.respond(HttpStatusCode.Created)
    }

}

fun Route.userSettings(userSettingsService: UserSettingsService) {
    get<UsersResource.IdResource.SettingsResource> {
        call.respond(
            userSettingsService.getUserSettings(it.parent.id)
        )
    }

    get<UsersResource.IdResource.SettingsResource.KeyResource> {
        call.respond(transaction {
            val settingId = userSettingsService.compositeKey(it.parent.parent.id, it.key)
            UserSettingsDao.findById(settingId)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    delete<UsersResource.IdResource.SettingsResource> {
        if (UserDao.findById(it.parent.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        userSettingsService.deleteUserSettings(it.parent.id)
        call.respond(HttpStatusCode.NoContent)
    }

    delete<UsersResource.IdResource.SettingsResource.KeyResource> {
        val settingId = userSettingsService.compositeKey(it.parent.parent.id, it.key)
        if (UserSettingsDao.findById(settingId) == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        userSettingsService.deleteUserSetting(settingId)
        call.respond(HttpStatusCode.NoContent)

    }

    post<UsersResource.IdResource.SettingsResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    post<UsersResource.IdResource.SettingsResource.KeyResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource.SettingsResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource.SettingsResource.KeyResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

}

fun Route.userTrips(userSavedTripService: UserSavedTripService) {
    get<UsersResource.IdResource.SavedTripsResource> {
        call.respond(
            userSavedTripService.getUserSavedTrips(it.parent.id)
        )
    }

    get<UsersResource.IdResource.SavedTripsResource.TripIdResource> {
        call.respond(transaction {
            val tripId = userSavedTripService.compositeKey(it.parent.parent.id, it.id)
            UserSavedTripDao.findById(tripId)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    delete<UsersResource.IdResource.SavedTripsResource> {
        if (UserDao.findById(it.parent.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        userSavedTripService.deleteUserTrips(it.parent.id)
        call.respond(HttpStatusCode.NoContent)
    }

    delete<UsersResource.IdResource.SavedTripsResource.TripIdResource> {
        val tripId = userSavedTripService.compositeKey(it.parent.parent.id, it.id)
        if (UserSavedTripDao.findById(tripId) == null) {
            call.respond(HttpStatusCode.NotFound)
        }
        userSavedTripService.deleteTrip(tripId)
        call.respond(HttpStatusCode.NoContent)
    }

    post<UsersResource.IdResource.SavedTripsResource.TripIdResource> {
        val tripId = userSavedTripService.compositeKey(it.parent.parent.id, it.id)
        if (UserSavedTripDao.findById(tripId) != null) {
            call.respond(HttpStatusCode.Conflict)
        }

        userSavedTripService.saveTrip(tripId)
        call.respond(HttpStatusCode.Created)
    }

    post<UsersResource.IdResource.SavedTripsResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource.SavedTripsResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource.SavedTripsResource.TripIdResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }
}

fun Route.userPois(userSavedPoiService: UserSavedPoiService) {
    get<UsersResource.IdResource.SavedPoiResource> {
        call.respond(
            userSavedPoiService.getUserSavedPois(it.parent.id)
        )
    }

    get<UsersResource.IdResource.SavedPoiResource.PoiIdResource> {
        call.respond(transaction {
            val tripId = userSavedPoiService.compositeKey(it.parent.parent.id, it.id)
            UserSavedPoiDao.findById(tripId)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    delete<UsersResource.IdResource.SavedPoiResource> {
        if (UserDao.findById(it.parent.id) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        userSavedPoiService.deleteUserPois(it.parent.id)
        call.respond(HttpStatusCode.NoContent)
    }

    delete<UsersResource.IdResource.SavedPoiResource.PoiIdResource> {
        val poiId = userSavedPoiService.compositeKey(it.parent.parent.id, it.id)
        if (UserSavedPoiDao.findById(poiId) == null) {
            call.respond(HttpStatusCode.NotFound)
        }

        userSavedPoiService.deleteUserPoi(poiId)
        call.respond(HttpStatusCode.NoContent)
    }

    post<UsersResource.IdResource.SavedPoiResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    post<UsersResource.IdResource.SavedPoiResource.PoiIdResource> {
        val poiId = userSavedPoiService.compositeKey(it.parent.parent.id, it.id)
        if (UserSavedPoiDao.findById(poiId) != null) {
            call.respond(HttpStatusCode.Conflict)
        }
        userSavedPoiService.saveUserPoi(poiId)
    }

    put<UsersResource.IdResource.SavedPoiResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }

    put<UsersResource.IdResource.SavedPoiResource.PoiIdResource> {
        call.respond(HttpStatusCode.NotImplemented)
    }
}