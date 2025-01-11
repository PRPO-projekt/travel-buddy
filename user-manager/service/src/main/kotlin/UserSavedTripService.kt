package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeID.Companion.invoke
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserSavedTripService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(UserSavedTripTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun compositeKey(userId: Long, tripId: Long): CompositeID {
        return CompositeID {
            it[UserSavedTripTable.userId] = EntityID<Long>(userId, UserTable)
            it[UserSavedTripTable.tripId] = tripId
        }
    }

    suspend fun getUserSavedTrips(userId: Long): List<UserSavedTrip> = dbQuery {
        UserSavedTripDao.find { UserSavedTripTable.userId eq EntityID(userId, UserTable) }.toList().map { it.toModel() }
    }

    suspend fun saveTrip(id: CompositeID) = dbQuery {
        val trip = UserSavedTripDao.findById(id)
        if (trip == null) {
            UserSavedTripDao.new {id}
        }
    }

    suspend fun deleteTrip(id: CompositeID) = dbQuery {
        val trip = UserSavedTripDao.findById(id)?.delete()
    }

    suspend fun findTrips(userId: Long): List<UserSavedTrip> = dbQuery {
        UserSavedTripDao.find{ UserSavedTripTable.userId eq EntityID(userId, UserTable) }.toList().map { it.toModel() }
    }

    suspend fun deleteUserTrips(userId: Long) = dbQuery {
        UserSavedTripDao.find { UserSavedTripTable.userId eq EntityID(userId, UserTable) }.forEach{ it.delete()}
    }
}