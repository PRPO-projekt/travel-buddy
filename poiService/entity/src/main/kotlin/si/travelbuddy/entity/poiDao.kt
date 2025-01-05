package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object poiTable : IntIdTable(name = "poi") {
    val poiId = integer("id")
    val poiName = varchar("name", 64)
    val poiDescription = varchar("description", 512)
    val lat = double("lat")
    val lon = double("lon")
    val idPostaje = varchar("idPostaje", 64)
}

@Serializable
data class poi(
    val id: Int?= null,
    val name: String ?= null,
    val description: String?= null,
    val lat: Double ?= null,
    val lon: Double ?= null,
    val idPostaje: String ?= null,
)

class poiDao(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, poiDao>(poiTable)

    var name by poiTable.poiName
    var description by poiTable.poiDescription
    var lat by poiTable.lat
    var lon by poiTable.lon
    var idPostaje by poiTable.idPostaje


    fun toModel(): poi = poi(
        id = id.value,
        name = name,
        description = description,
        lat = lat,
        lon = lon,
        idPostaje = idPostaje
    )}