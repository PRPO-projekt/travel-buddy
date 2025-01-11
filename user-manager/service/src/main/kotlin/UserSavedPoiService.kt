package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeID.Companion.invoke
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserSavedPoiService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(UserSavedPoiTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun compositeKey(userId: Long, poiId: Long): CompositeID {
        return CompositeID {
            it[UserSavedPoiTable.userId] = EntityID<Long>(userId, UserTable)
            it[UserSavedPoiTable.poiId] = poiId
        }
    }

    suspend fun getUserSavedPois(userId: Long): List<UserSavedPoi> = dbQuery {
        UserSavedPoiDao.find{ UserSavedPoiTable.userId eq EntityID(userId, UserTable) }.toList().map { it.toModel() }
    }

    suspend fun saveUserPoi(id: CompositeID) = dbQuery {
        val trip = UserSavedPoiDao.findById(id)
        if (trip == null) {
            UserSavedPoiDao.new {id}
        }
    }


    suspend fun deleteUserPois(userId: Long) = dbQuery {
        /**
         * Deletes ALL user points of interest
         *
         * @param userId user which will have all points of interest deleted
         * @returns Unit
         * */
        UserSavedPoiDao.find { UserSavedPoiTable.userId eq EntityID(userId, UserTable) }.forEach { it.delete() }
    }

    suspend fun deleteUserPoi(id: CompositeID) = dbQuery {
        /**
         * Deletes specific user point of interest
         *
         * @param id composite id of userId and poiId (can be generated using compositeKey)
         * @returns Unit
         */
        val trip = UserSavedPoiDao.findById(id)?.delete()
    }

    suspend fun findUserPois(userId: Long): List<UserSavedPoi> = dbQuery {
        /**
         * Finds all points of interest for a specific user
         *
         * @param userId user for which all points of interest will be returned
         * @returns returns list of userIds and poiIds of type List<UserSavedPoi>
         */
        UserSavedPoiDao.find{ UserSavedPoiTable.userId eq EntityID(userId, UserTable) }.toList().map { it.toModel() }
    }
}