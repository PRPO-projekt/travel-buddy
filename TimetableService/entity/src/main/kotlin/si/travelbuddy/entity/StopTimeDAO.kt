package si.travelbuddy.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.time
import java.util.UUID

object StopTimeTable : UUIDTable(name = "stop_time") {
    val tripId = reference("trip_id", TripTable)

    val arrivalTime = time("arrival_time")
    val departureTime = time("departure_time")

    val stopId = reference("stop_id", StopTable)
}

@Serializable
data class StopTime(
    val tripId: String,
    val stopId: String,
    val arrivalTime: String,
    val departureTime: String,
)

class StopTimeDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<StopTimeDAO>(StopTimeTable)

    var arrivalTime by StopTimeTable.arrivalTime
    var departureTime by StopTimeTable.departureTime

    var tripId by TripDAO referencedOn StopTimeTable.tripId
    var stopId by StopDAO referencedOn StopTimeTable.stopId

    fun toModel(): StopTime = StopTime(
        "", "", arrivalTime.toString(), departureTime.toString()
    )
}