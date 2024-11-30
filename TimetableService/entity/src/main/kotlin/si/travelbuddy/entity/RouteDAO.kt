package si.travelbuddy.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object RouteTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("route_id", 32).entityId()

    val routeShortName = varchar("route_short_name", 32).nullable()
    val routeLongName = varchar("route_long_name", 32).nullable()

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)
}

data class Route(
    val id: String,
    val shortName: String? = null,
    val longName: String? = null,
)

class RouteDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, RouteDAO>(RouteTable)

    var routeShortName by RouteTable.routeShortName
    var routeLongName by RouteTable.routeLongName

    fun toModel(): Route = Route(id.toString(), routeShortName, routeLongName)
}