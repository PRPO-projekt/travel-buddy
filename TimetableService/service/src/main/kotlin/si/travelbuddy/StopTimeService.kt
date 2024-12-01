package si.travelbuddy

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import si.travelbuddy.dto.StopTimeDto
import si.travelbuddy.entity.*
import java.time.DateTimeException
import java.time.LocalTime

class StopTimeService(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(StopTimeTable)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(stopTime: StopTimeDto) = transaction {
        val stop = StopDao.findById(stopTime.stopId!!)!!
        val trip = TripDao.findById(stopTime.tripId!!)!!

        var arrTime = LocalTime.MAX
        try {
            arrTime = LocalTime.ofSecondOfDay(stopTime.arrivalTime!!.toLong())
        }
        catch (_: DateTimeException) {

        }

        var depTime = LocalTime.MAX
        try {
            depTime = LocalTime.ofSecondOfDay(stopTime.departureTime!!.toLong())
        }
        catch (_: DateTimeException) {

        }

        StopTimeDao.new {
            arrivalTime = arrTime
            departureTime = depTime
            tripId = trip
            stopId = stop
        }
    }
}