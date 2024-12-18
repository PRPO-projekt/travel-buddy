package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.RouteDto

import si.travelbuddy.entity.RouteDao
import si.travelbuddy.entity.RouteTable

class RouteService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(RouteTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(route: RouteDto) = transaction {
        RouteDao.new(route.id) {
            routeShortName = route.shortName
            routeLongName = route.longName
        }
    }
}