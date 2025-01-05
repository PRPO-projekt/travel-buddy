package si.travelbuddy
import si.travelbuddy.dto.poiDto
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import si.travelbuddy.entity.RouteDao
import si.travelbuddy.entity.RouteTable

class poiService(private val database: Database) {
    init{
        transaction(database) {
            SchemaUtils.create(poiTable)
        }
    }
}