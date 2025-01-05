import dto.DepartureDelayDto
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class DepartureDelayService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(DepartureDelayTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(departureDelay: DepartureDelayDto) = transaction {
        val dur = Duration.parse(departureDelay.delay)

        DepartureDelayDao.new {
            delay = dur.toJavaDuration()
            stopId = departureDelay.stopId
            userId = departureDelay.userId
        }
    }

    private suspend fun findByStopId(stopId: String) = dbQuery {
        DepartureDelayDao.find {
            DepartureDelayTable.stopId eq stopId
        }
    }

    suspend fun averageDelay(stopId: String? = null) = dbQuery {
        val delays = if (stopId == null) { DepartureDelayDao.all() } else { findByStopId(stopId) }

        delays.map { it.delay.toMinutes() }.average()
    }
}