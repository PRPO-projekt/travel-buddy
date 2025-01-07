package si.travelbuddy

//import org.jetbrains.exposed.sql.update

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.poiDto
import si.travelbuddy.entity.*

class poiService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(poiTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun getById(id: Int): poi? = dbQuery {
        poiDao.findById(id)?.toModel()
    }
    suspend fun delete(id: Int) = dbQuery {
        poiTable.deleteWhere { poiTable.id eq id }
    }

    fun create(poi: poiDto) = transaction {
        poiDao.new(poi.id) {
            name = poi.name.toString()
            description = poi.description.toString()
            lon = poi.lon?.toDouble() ?: 0.0
            lat = poi.lat?.toDouble() ?: 0.0
            idPostaje = poi.idPostaje?.toInt() ?: -1
        }.id.value
    }



}