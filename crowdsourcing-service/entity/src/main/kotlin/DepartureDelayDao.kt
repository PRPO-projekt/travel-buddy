import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object DepartureDelayTable : IntIdTable() {
    val expectedTime = datetime("expected_time")
    val actualTime = datetime("actual_time")

    val userId = varchar("user_id", 32).nullable()
}

@Serializable
data class DepartureDelay(
    val id: Int,
    val expectedTime: String,
    val actualTime: String,
    val userId: String?
)

class DepartureDelayDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DepartureDelayDao>(DepartureDelayTable)

    var expectedTime by DepartureDelayTable.expectedTime
    var actualTime by DepartureDelayTable.actualTime

    var userId by DepartureDelayTable.userId

    fun toModel(): DepartureDelay = DepartureDelay(id.value, expectedTime.toString(), actualTime.toString(), userId.toString())
}