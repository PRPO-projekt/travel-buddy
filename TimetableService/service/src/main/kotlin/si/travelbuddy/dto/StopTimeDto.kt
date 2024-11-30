package si.travelbuddy.dto

data class StopTimeDto(
    val tripId: String? = null,
    val arrivalTime: Int? = null,
    val departureTime: Int? = null,
    val stopId: String? = null
)
