package si.travelbuddy

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.Reference
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserSavedPoiTable: CompositeIdTable() {
    val userId = reference("user_id", UserTable).entityId()
    val poiId = long("poi_id").entityId()
}

class UserSavedPoiDao(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<UserSavedPoiDao>(UserSavedPoiTable)

    val userId by UserSavedPoiTable.userId
    var poiId by UserSavedPoiTable.poiId

    fun toModel(): UserSavedPoi = UserSavedPoi(userId.value.value, poiId.value)
}

@Serializable
data class UserSavedPoi(
    val userId: Long,
    val poiId: Long
)