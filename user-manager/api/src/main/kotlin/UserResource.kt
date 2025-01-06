package si.travelbuddy.userManager

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.UserDao
import si.travelbuddy.UserSavedTripDao
import si.travelbuddy.UserService
import si.travelbuddy.UserSettingsDao
import si.travelbuddy.UserSettingsService

@Resource("/users")
class UsersResource {
    @Resource("{id}")
    class IdResource(val parent: UsersResource, val id: Long) {
        @Resource("settings")
        class SettingsResource (val parent: IdResource) {
            @Resource("{key}")
            class KeyResource (val parent: SettingsResource, val key: String) {}
        }
        @Resource("saved-trips")
        class SavedTripsResource(val parent: IdResource) {
            @Resource("{key}")
            class KeyResource (val parent: SavedTripsResource, val key: String) {}
        }
        @Resource("saved-poi")
        class SavedPoiResource(val parent: IdResource) {
            @Resource("{key}")
            class KeyResource (val parent: SavedPoiResource, val key: String) {}
        }
    }
}

fun Route.users(userService: UserService, userSettingsService: UserSettingsService) {
    get<UsersResource> {
        user ->
        call.respond(transaction{
            UserDao.all().toList()
        }.map { userDao -> userDao.toModel() })
    }

    get<UsersResource.IdResource> {
        userId -> call.respond(transaction{
            UserDao.findById(userId.id)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    get<UsersResource.IdResource.SettingsResource> {
        user -> call.respond(transaction{
            UserSettingsDao.all().toList()
        }.map { userSettingsDao -> userSettingsDao.toModel() })
    }

    get<UsersResource.IdResource.SettingsResource.KeyResource> {
        user -> call.respond(transaction{
            val settingId = userSettingsService.settingsCompositeKey(user.parent.parent.id, user.key)
            UserSettingsDao.findById(settingId)?.toModel() ?: HttpStatusCode.NotFound
        })
    }

    get<UsersResource.IdResource.SavedTripsResource> {
        user -> call.respond(transaction{
            UserSavedTripDao.all().toList()
        }.map { userSavedTripDao -> userSavedTripDao.toModel() })
    }
}