package dto

import kotlinx.serialization.Serializable

@Serializable
data class DepartureDelayDto(
    val expectedTime: String,
    val actualTime: String,
    val userId: String
)
