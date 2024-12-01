package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object TripTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("trip_id", 32).entityId()

    val routeId = reference("route_id", RouteTable)
    val tripHeadsign = varchar("trip_headsign", 32).nullable()
    val blockId = integer("block_id").nullable()

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)
}

@Serializable
data class Trip(
    val id: String,
    val routeId: String,
    val tripHeadsign: String?,
    val blockId: Int?
)

class TripDao(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TripDao>(TripTable)

    var routeId by RouteDao referencedOn TripTable.routeId
    var tripHeadsign by TripTable.tripHeadsign
    var blockId by TripTable.blockId

    fun toModel(): Trip = Trip(id.value, routeId.id.toString(), tripHeadsign, blockId)
}