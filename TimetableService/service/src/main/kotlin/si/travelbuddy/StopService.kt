package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.entity.StopDao
import si.travelbuddy.entity.StopTable

class StopService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(StopTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(stop: StopDto): String = transaction {
        StopDao.new(stop.id) {
            name = stop.name
            lat = stop.lat
            lon = stop.lon
        }.id.value
    }

    suspend fun update(id: String, stop: StopDto) = dbQuery {
        StopDao.findByIdAndUpdate(id) { it ->
            it.name = stop.name
            it.lat = stop.lat
            it.lon = stop.lon
        }
    }

    suspend fun delete(id: String) = dbQuery {
        StopTable.deleteWhere { StopTable.id eq id }
    }
}