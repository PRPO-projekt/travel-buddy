package si.travelbuddy.entity

import io.ktor.server.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time
import java.time.LocalDateTime
import java.time.LocalTime

object StopTimeTable : CompositeIdTable("stop_time") {
    val tripId = reference("trip_id", TripTable)

    val arrivalTime = time("arrival_time")
    val departureTime = time("departure_time")

    val stopId = reference("stop_id", StopTable)

    override val primaryKey = PrimaryKey(tripId, stopId)
}

@Serializable
data class StopTime(
    val tripId: String,
    val stopId: String,
    val arrivalTime: String,
    val departureTime: String,
)

class StopTimeDAO(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<StopTimeDAO>(StopTimeTable)

    var arrivalTime by StopTimeTable.arrivalTime
    var departureTime by StopTimeTable.departureTime

    fun toModel(): StopTime = StopTime(
        "", "", arrivalTime.toString(), departureTime.toString()
    )
}