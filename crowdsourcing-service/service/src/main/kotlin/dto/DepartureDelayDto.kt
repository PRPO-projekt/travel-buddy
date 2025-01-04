package dto

data class DepartureDelayDto(
    val expectedTime: String,
    val actualTime: String,
    val userId: String
)
