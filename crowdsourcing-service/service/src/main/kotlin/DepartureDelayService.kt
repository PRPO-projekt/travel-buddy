import dto.DepartureDelayDto
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class DepartureDelayService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(DepartureDelayTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(departureDelay: DepartureDelayDto) = transaction {
        DepartureDelayDao.new {
            expectedTime = LocalDateTime.parse(departureDelay.expectedTime)
            actualTime = LocalDateTime.parse(departureDelay.actualTime)
            userId = departureDelay.userId
        }
    }

    suspend fun averageDelay() = dbQuery {
        val actualAverage = DepartureDelayTable.actualTime.avg()
        val expectedAverage = DepartureDelayTable.expectedTime.avg()

        val p = DepartureDelayTable.select(actualAverage, expectedAverage).map {
            Pair(it[actualAverage], it[expectedAverage])
        }[0]

        val res = p.first!! - p.second!!
        res
    }
}