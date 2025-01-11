package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.SettingDto

class UserSettingsService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(UserSettingsTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun compositeKey(userId: Long, settingKey: String): CompositeID {
        return CompositeID {
            it[UserSettingsTable.userId] = EntityID<Long>(userId, UserTable)
            it[UserSettingsTable.settingKey] = settingKey
        }
    }

    suspend fun getUserSettings(userId: Long): List<UserSettings> = dbQuery {
        UserSettingsDao.find { UserSettingsTable.userId eq EntityID(userId, UserTable) }.toList().map { it.toModel()}
    }

    suspend fun changeUserSetting(settingDto: SettingDto) = dbQuery {
        val settingId = compositeKey(settingDto.userId, settingDto.settingKey)

        UserSettingsDao.findByIdAndUpdate(settingId) {
            it.settingValue = settingDto.settingValue
        }
    }


    suspend fun deleteUserSettings(userId: Long) = dbQuery{
        UserSettingsDao.find{ UserSettingsTable.userId eq EntityID(userId, UserTable) }.forEach { it.delete() }
    }

    suspend fun deleteUserSetting(id: CompositeID) = dbQuery {
        UserSettingsDao.findById(id)?.delete()
    }

    suspend fun deleteSetting(key: String) = dbQuery {
        UserSettingsDao.find { UserSettingsTable.settingKey eq key }.forEach { it.delete() }
    }
}