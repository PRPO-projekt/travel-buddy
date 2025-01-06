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
import org.jetbrains.exposed.sql.ReferenceOption

object UserSettingsTable: CompositeIdTable() {
    val userId = reference("user_id", UserTable).entityId()
    val settingKey = varchar("setting_key", 32).entityId()
    val settingValue = varchar("setting_value", 32)
}

class UserSettingsDao(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<UserSettingsDao>(UserSettingsTable)

    val userId by UserSettingsTable.userId
    val settingKey by UserSettingsTable.settingKey
    var settingValue by UserSettingsTable.settingValue

    fun toModel(): UserSettings = UserSettings(userId.value.value, settingKey.value, settingValue)
}

@Serializable
data class UserSettings(
    val userId: Long,
    val settingKey: String,
    val settingValue: String
)