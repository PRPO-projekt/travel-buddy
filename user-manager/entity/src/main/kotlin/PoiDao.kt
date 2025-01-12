package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable


object poiTable : LongIdTable() {
    val poiName = varchar("name", 64)
    val poiDescription = varchar("description", 512)
    val lat = double("lat")
    val lon = double("lon")
    val stopId = varchar("stop_id", 64)
}

@Serializable
data class poi(
    val id: Long,
    val name: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val stopId: String,
)

class poiDao(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, poiDao>(poiTable)

    var name by poiTable.poiName
    var description by poiTable.poiDescription
    var lat by poiTable.lat
    var lon by poiTable.lon
    var stopId by poiTable.stopId


    fun toModel(): poi = poi(
        id = id.value,
        name = name,
        description = description,
        lat = lat,
        lon = lon,
        stopId = stopId
    )}