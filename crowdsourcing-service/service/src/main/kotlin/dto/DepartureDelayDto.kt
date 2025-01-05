package dto

import kotlinx.serialization.Serializable

@Serializable
data class DepartureDelayDto(
    val delay: String,
    val userId: String,
    val stopId: String
)
