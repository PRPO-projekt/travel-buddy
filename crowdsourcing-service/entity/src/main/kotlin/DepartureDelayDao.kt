import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.duration
import kotlin.time.toKotlinDuration

object DepartureDelayTable : IntIdTable() {
    val delay = duration("delay")

    val userId = varchar("user_id", 32).nullable()
    val stopTimeId = varchar("stop_time_id", 32)
}

@Serializable
data class DepartureDelay(
    val id: Int,
    val delay: kotlin.time.Duration,
    val userId: String?,
    val stopTimeId: String
)

class DepartureDelayDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DepartureDelayDao>(DepartureDelayTable)

    var delay by DepartureDelayTable.delay

    var userId by DepartureDelayTable.userId
    var stopTimeId by DepartureDelayTable.stopTimeId

    fun toModel(): DepartureDelay = DepartureDelay(
        id.value,
        delay.toKotlinDuration(),
        userId.toString(),
        stopTimeId
    )
}