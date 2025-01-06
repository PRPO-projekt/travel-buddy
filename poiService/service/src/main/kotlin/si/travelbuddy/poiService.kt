package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.poiDto
import si.travelbuddy.entity.poi
import si.travelbuddy.entity.poiDao

class poiService(private val database: Database) {
    init {
        transaction(database) {
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun getById(id: Int): poi? = dbQuery {
        poiDao.findById(id)?.toModel()
    }

    fun create(poi: poiDto) = transaction {
        poiDao.new(poi.id) {
            name = poi.name.toString()
            description = poi.description.toString()
            lon = poi.lon?.toDouble() ?: 0.0
            lat = poi.lat?.toDouble() ?: 0.0
            idPostaje = poi.idPostaje.toString()
        }.id.value
    }



}