package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopDto
import si.travelbuddy.entity.Stop
import si.travelbuddy.entity.StopTable

class StopService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(StopTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(stop: StopDto): Int = transaction {
        Stop.new {
            stopId = stop.stopId
            code = stop.code
            name = stop.name
        }.id.value
    }
}