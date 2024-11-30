package si.travelbuddy.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object TripTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("trip_id", 32).entityId()

    val routeId = reference("route_id", RouteTable.id)
    val tripHeadsign = varchar("trip_headsign", 32).nullable()
    val blockId = integer("block_id").nullable()

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)
}

data class Trip(
    val id: String,
    val routeId: String,
    val tripHeadsign: String?,
    val blockId: Int?
)

class TripDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TripDAO>(TripTable)

    var routeId by RouteDAO referencedOn TripTable.routeId
    var tripHeadsign by TripTable.tripHeadsign
    var blockId by TripTable.blockId

    fun toModel() = Trip(id.value, routeId.toString(), tripHeadsign, blockId)
}