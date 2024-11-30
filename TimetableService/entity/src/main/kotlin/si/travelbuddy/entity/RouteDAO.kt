package si.travelbuddy.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object RouteTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("route_id", 32).entityId()

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)
}


class RouteDAO(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, RouteDAO>(RouteTable)
}