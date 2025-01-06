package si.travelbuddy

import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class UserSettingsService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(UserSettingsTable)
        }
    }

    fun settingsCompositeKey(userId: Long, settingKey: String): CompositeID {
        return CompositeID {
            it[UserSettingsTable.userId] = EntityID<Long>(userId, UserTable)
            it[UserSettingsTable.settingKey] = settingKey
        }
    }

    fun changeSetting(settingId: CompositeID, settingValue: String) {
        UserSettingsDao.findByIdAndUpdate(settingId) {
            it.settingValue = settingValue
        }
    }
}