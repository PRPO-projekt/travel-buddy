package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import si.travelbuddy.dto.TripDto
import si.travelbuddy.entity.*

class TripService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(TripTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(trip: TripDto) = transaction {
        val route = RouteDao.findById(trip.routeId)!!

        TripDao.new(trip.id) {
            routeId = route
        }
    }

    suspend fun update(id: String, trip: TripDto) = dbQuery {
        TripTable.update({ TripTable.id eq id }) {
            it[routeId] = trip.routeId
        }
    }

    suspend fun delete(id: String) = dbQuery {
        TripTable.deleteWhere { TripTable.id eq id }
    }
}