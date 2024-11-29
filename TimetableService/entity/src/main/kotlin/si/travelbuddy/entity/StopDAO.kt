package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StopTable : IntIdTable() {
    val stopId = varchar("stop_id", 10).index()
    val name = varchar("stop_name", 50)

    var lat = double("stop_lat")
    var lon = double("stop_lon")
}

@Serializable
data class Stop(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double
)

class StopDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StopDAO>(StopTable)

    var stopId by StopTable.stopId
    var name by StopTable.name

    var lat by StopTable.lat
    var lon by StopTable.lon

    fun toModel(): Stop = Stop(stopId, name, lat, lon)
}