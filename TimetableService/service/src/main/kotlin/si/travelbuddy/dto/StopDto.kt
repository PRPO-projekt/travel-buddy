package si.travelbuddy.dto

data class StopDto(
    val stopId: String,
    val code: String,
    val name: String,
    val desc: String,

    val lat: Double,
    val lon: Double
)