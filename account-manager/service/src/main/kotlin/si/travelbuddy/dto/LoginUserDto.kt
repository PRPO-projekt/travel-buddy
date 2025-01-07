package si.travelbuddy.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserDto (
    val username: String,
    val password: String,
)
