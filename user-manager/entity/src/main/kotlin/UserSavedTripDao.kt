package si.travelbuddy

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object UserSavedTripTable: LongIdTable() {
    val userId = long("user_id")
    val tripId = long("trip_id")
}

class UserSavedTripDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserSavedTripDao>(UserSavedTripTable)

    var userId by UserSavedTripTable.userId
    var tripId by UserSavedTripTable.tripId

    fun toModel(): UserSavedTrip = UserSavedTrip(id.value, userId, tripId)
}

@Serializable
data class UserSavedTrip(
    val id: Long,
    val userId: Long,
    val tripId: Long
)