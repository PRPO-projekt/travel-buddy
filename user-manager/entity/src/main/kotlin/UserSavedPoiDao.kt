package si.travelbuddy

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.Reference
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserSavedPoiTable: LongIdTable() {
    val userId = long("user_id")
    val poiId = long("poi_id")
}

class UserSavedPoiDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserSavedPoiDao>(UserSavedPoiTable)

    val userId by UserSavedPoiTable.userId
    var poiId by UserSavedPoiTable.poiId

    fun toModel(): UserSavedPoi = UserSavedPoi(id.value, userId, poiId)
}

@Serializable
data class UserSavedPoi(
    val id: Long,
    val userId: Long,
    val poiId: Long
)