package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object StopTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("stop_id", 10).entityId()
    val name = varchar("stop_name", 50)

    var lat = double("stop_lat")
    var lon = double("stop_lon")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

@Serializable
data class Stop(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double
)

class StopDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, StopDAO>(StopTable)

    var name by StopTable.name
    var lat by StopTable.lat
    var lon by StopTable.lon

    fun toModel(): Stop = Stop(id.value, name, lat, lon)
}