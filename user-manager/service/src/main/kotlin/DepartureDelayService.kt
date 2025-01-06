import dto.DepartureDelayDto
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
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
            stopTimeId = departureDelay.stopTimeId
            userId = departureDelay.userId
        }
    }

    private suspend fun findByStopTimeId(stopTimeId: String) = dbQuery {
        DepartureDelayDao.find {
            DepartureDelayTable.stopTimeId eq stopTimeId
        }
    }

    suspend fun averageDelay(stopTimeId: String? = null) = dbQuery {
        val delays = if (stopTimeId == null) {
            DepartureDelayDao.all()
        } else {
            findByStopTimeId(stopTimeId)
        }

        delays.map { it.delay.toMinutes() }.average()
    }
}