package si.travelbuddy

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object UserSavedTripTable: CompositeIdTable() {
    val userId = reference("user_id", UserTable).entityId()
    val tripId = long("trip_id").entityId()
}

class UserSavedTripDao(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<UserSavedTripDao>(UserSavedTripTable)

    var userId by UserSavedTripTable.userId
    var tripId by UserSavedTripTable.tripId

    fun toModel(): UserSavedTrip = UserSavedTrip(userId.value.value, tripId.value)
}

@Serializable
data class UserSavedTrip(
    val userId: Long,
    val tripId: Long
)