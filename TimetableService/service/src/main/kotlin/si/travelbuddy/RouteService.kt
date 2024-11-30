package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.RouteDto

import si.travelbuddy.entity.RouteDAO
import si.travelbuddy.entity.RouteTable
import si.travelbuddy.entity.StopTable

class RouteService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(RouteTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(route: RouteDto) = transaction {
        RouteDAO.new(route.id) {
        }
    }
}