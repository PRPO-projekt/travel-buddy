package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.TripDto
import si.travelbuddy.entity.*
import si.travelbuddy.entity.TripTable.routeId

class TripService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(TripTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(trip: TripDto) = transaction {
        val route = RouteDAO.findById(trip.routeId)!!

        TripDAO.new(trip.id) {
            routeId = route
        }
    }
}