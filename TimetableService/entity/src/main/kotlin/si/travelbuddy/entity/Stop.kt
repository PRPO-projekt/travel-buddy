package si.travelbuddy.entity

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StopTable : IntIdTable() {
    val stopId = varchar("stop_id", 10).index()
    val code = varchar("stop_code", 10)
    val name = varchar("stop_name", 50)
    val desc = varchar("stop_desc", 50)

    var lat = double("stop_lat")
    var lon = double("stop_lon")
}

class Stop(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Stop>(StopTable)

    var stopId by StopTable.stopId
    var code by StopTable.code
    var name by StopTable.name
}